package com.demo.drive.data.paging.drive

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.model.ListParams
import com.demo.drive.core.model.Result
import com.demo.drive.data.mapper.toDriveFileList
import com.demo.drive.data.network.DriveApi

internal class FilesPagingSource(
    private val driveApi: DriveApi,
    private val listParams: ListParams
) : PagingSource<String, DriveFile>() {
    override fun getRefreshKey(state: PagingState<String, DriveFile>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, DriveFile> {
        //nextPageToken from previous response to load this page, initially null
        val currentNextPageToken = params.key

        return when (val filesResult = driveApi.list(listParams, currentNextPageToken)) {
            is Result.Error -> {
                LoadResult.Error(
                    filesResult.exception
                )
            }
            is Result.Success -> {
                LoadResult.Page(
                    data = filesResult.data.toDriveFileList(),
                    prevKey = null,//no previous page
                    nextKey = filesResult.data.nextPageToken
                )
            }
        }


    }
}