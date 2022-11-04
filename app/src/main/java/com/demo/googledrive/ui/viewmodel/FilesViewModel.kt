package com.demo.googledrive.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.usecase.drive.ListFilesUseCase
import com.demo.googledrive.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.demo.drive.core.model.Result
import com.demo.drive.core.usecase.drive.DownloadFileUseCase
import com.demo.drive.core.usecase.drive.LogoutUseCase
import kotlinx.coroutines.*

@HiltViewModel
class FilesViewModel @Inject constructor(
    application: Application,
    private val listFilesUseCase: ListFilesUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val pathManager: PathManager
) : AndroidViewModel(application) {

    private var searchDelayJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val files = pathManager.current.flatMapLatest {
        val listParams = when (it) {
            is Folder.List -> {
                buildFolderListParams(it.id)
            }
            is Folder.Search -> {
                buildSearchListParams(it.query)
            }
        }
        listFilesUseCase.listFiles(listParams)
    }.cachedIn(viewModelScope)

    val navigationRoute = pathManager.current.mapLatest {
        pathManager.buildDisplayValue()
    }

    private val _logoutResult = MutableSharedFlow<Result<Unit>>(replay = 0)
    val logoutResult: SharedFlow<Result<Unit>> get() = _logoutResult

    private val _downloadStartResult = MutableSharedFlow<Result<Long>>(replay = 0)
    val downloadStartResult: SharedFlow<Result<Long>> get() = _downloadStartResult

    fun openFolder(driveFile: DriveFile) {
        driveFile.toFolder()?.let {
            pathManager.push(it)
        }
    }

    fun searchFiles(query: String?) {
        searchDelayJob?.cancel()
        if (query.isNullOrEmpty()) {
            pathManager.reset(PathManager.getListRootFolder(getApplication()))
        } else {
            searchDelayJob = viewModelScope.launch {
                delay(500)
                pathManager.reset(PathManager.getSearchRootFolder(query))
            }
        }
    }

    fun navigateBack(): Boolean {
        return pathManager.pop() != null
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            _logoutResult.emit(logoutUseCase.logout())
        }
    }

    fun downloadFile(driveFile: DriveFile) = viewModelScope.launch {
        _downloadStartResult.emit(downloadFileUseCase.downloadFile(driveFile))
    }
}