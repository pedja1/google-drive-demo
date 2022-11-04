package com.demo.drive.core.usecase.drive

import androidx.paging.PagingData
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.model.ListParams
import kotlinx.coroutines.flow.Flow

interface ListFilesUseCase {
    fun listFiles(listParams: ListParams): Flow<PagingData<DriveFile>>
}