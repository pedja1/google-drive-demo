package com.demo.drive.core.repository

import androidx.paging.PagingSource
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.model.ListParams
import com.demo.drive.core.model.Result

interface DriveRepository {
    fun list(listParams: ListParams): PagingSource<String, DriveFile>
}