package com.demo.googledrive.utils

import android.content.Context
import com.demo.drive.core.model.DriveFile
import com.demo.googledrive.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PathManager(rootFolder: Folder) {

    companion object {
        private const val LIST_ROOT_FOLDER_ID = "root"

        fun getListRootFolder(context: Context): Folder = Folder.List(
            name = context.getString(R.string.root_folder_name),
            id = LIST_ROOT_FOLDER_ID,
        )

        fun getSearchRootFolder(query: String): Folder = Folder.Search(
            query = query,
            name = query
        )
    }

    private val path: MutableList<Folder> = ArrayDeque()

    private val _current: MutableStateFlow<Folder>
    val current: StateFlow<Folder> get() = _current

    init {
        path.add(rootFolder)
        _current = MutableStateFlow(rootFolder)
    }

    fun push(folder: Folder) {
        path.add(folder)
        _current.value = folder
    }

    fun pop(): Folder? {
        if (path.size == 1) {
            return null
        }
        val removed = path.removeLast()
        _current.value = path.last()
        return removed
    }

    fun reset(folder: Folder) {
        path.clear()
        path.add(folder)
        _current.value = folder
    }

    fun getCurrent(): Folder {
        return path.last()
    }

    fun getPath(): List<Folder> {
        return path
    }

    fun buildDisplayValue(): String {
        return path.joinToString(separator = "/") {
            it.name
        }
    }
}

sealed class Folder(
    val name: String,
) {
    class Search(val query: String, name: String) : Folder(name)
    class List(val id: String, name: String) : Folder(name)
}

fun DriveFile?.toFolder(): Folder? {
    val id = this?.id ?: return null
    val name = this.name ?: return null
    return Folder.List(id, name)
}