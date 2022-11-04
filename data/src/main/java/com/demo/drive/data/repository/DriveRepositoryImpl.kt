package com.demo.drive.data.repository

import androidx.paging.PagingSource
import com.demo.drive.core.model.*
import com.demo.drive.core.repository.DriveRepository
import com.demo.drive.data.network.DriveApi
import com.demo.drive.data.paging.drive.FilesPagingSource
import javax.inject.Inject

internal class DriveRepositoryImpl @Inject constructor(private val driveApi: DriveApi) :
    DriveRepository {

    override fun list(listParams: ListParams): PagingSource<String, DriveFile> {
        return FilesPagingSource(driveApi, listParams)
    }
}