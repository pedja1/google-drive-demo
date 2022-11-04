package com.demo.drive.data.drive

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.demo.drive.core.model.DriveFile

class MockFilePagingSource(private val loadResult: LoadResult<String, DriveFile>) : PagingSource<String, DriveFile>() {

    override fun getRefreshKey(state: PagingState<String, DriveFile>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, DriveFile> {
        return loadResult
    }
}