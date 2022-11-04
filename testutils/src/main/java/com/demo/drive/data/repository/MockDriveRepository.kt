package com.demo.drive.data.repository

import androidx.paging.PagingSource
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.model.ListParams
import com.demo.drive.core.repository.DriveRepository
import com.demo.drive.data.drive.MockFilePagingSource

class MockDriveRepository : DriveRepository {

    private lateinit var loadResult : PagingSource.LoadResult<String, DriveFile>

    override fun list(listParams: ListParams): PagingSource<String, DriveFile> {
        return MockFilePagingSource(loadResult)
    }

    fun setLoadResultForList(loadResult: PagingSource.LoadResult<String, DriveFile>) {
        this.loadResult = loadResult
    }
}