package com.demo.drive.data.paging.drive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.demo.drive.core.model.ListParams
import com.demo.drive.data.network.DriveApi
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.demo.drive.core.model.Result
import com.demo.drive.data.mapper.toDriveFileList
import com.demo.drive.data.model.ListResponseDTO
import com.google.common.truth.Truth.assertThat


class FilesPagingSourceTest {

    private val listSuccessResponse = Result.Success(ListResponseDTO(nextPageToken = "next page key"))
    private val listErrorResponse = Result.Error<ListResponseDTO>(Exception("error"))

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    internal lateinit var driveApi: DriveApi

    @MockK
    internal lateinit var listParams: ListParams

    private lateinit var filesPagingSource: FilesPagingSource

    @Before
    fun setup() {
        filesPagingSource = FilesPagingSource(driveApi, listParams)
    }

    @Test
    fun `load returns error when api returns error`() = runBlocking {
        every {
            runBlocking {
                driveApi.list(any(), any())
            }
        } returns listErrorResponse

        assertThat(
            filesPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        ).isInstanceOf(PagingSource.LoadResult.Error::class.java)

    }

    @Test
    fun `load returns success when api returns success`() = runBlocking {
        every {
            runBlocking {
                driveApi.list(any(), any())
            }
        } returns listSuccessResponse

        assertThat(
            filesPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        ).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listSuccessResponse.data.toDriveFileList(),
                prevKey = null,
                nextKey = listSuccessResponse.data.nextPageToken
            )
        )
    }
}