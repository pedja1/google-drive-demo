package com.demo.drive.data.network.impl

import com.demo.drive.core.model.ListParams
import com.demo.drive.core.model.Result
import com.demo.drive.data.model.ErrorResponseDTO
import com.demo.drive.data.model.ListResponseDTO
import com.demo.drive.data.network.DriveApi
import com.demo.drive.data.network.GoogleDriveService
import com.demo.drive.data.network.ApiClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class DriveApiImpl @Inject constructor(private val apiClient: ApiClient) : DriveApi {

    private val googleDriveService = apiClient.createService(GoogleDriveService::class.java)

    override suspend fun list(listParams: ListParams, pageToken: String?): Result<ListResponseDTO> {
        return execute {
            googleDriveService.list(
                listParams.query,
                listParams.fields,
                listParams.orderBy,
                pageToken,
                listParams.pageSize
            )
        }
    }

    private suspend fun <T> execute(
        function: suspend () -> T
    ): Result<T> {
        return try {
            Result.Success(function())
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()
            val data = if (errorBody != null) {
                val converter: Converter<ResponseBody, ErrorResponseDTO> =
                    apiClient.responseBodyConverter(ErrorResponseDTO::class.java, emptyArray())
                try {
                    converter.convert(errorBody)
                } catch (e: IOException) {
                    null
                }
            } else {
                null
            }
            Result.Error(data?.error?.message?.let { Exception(it) } ?: e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}