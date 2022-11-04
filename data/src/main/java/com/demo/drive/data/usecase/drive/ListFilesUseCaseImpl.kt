package com.demo.drive.data.usecase.drive

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.model.ListParams
import com.demo.drive.core.repository.DriveRepository
import com.demo.drive.core.usecase.drive.ListFilesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListFilesUseCaseImpl @Inject constructor(private val driveRepository: DriveRepository) : ListFilesUseCase {
    override fun listFiles(listParams: ListParams): Flow<PagingData<DriveFile>> {
        return Pager(
            config = PagingConfig(
                listParams.pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { driveRepository.list(listParams) }
        ).flow
    }
}