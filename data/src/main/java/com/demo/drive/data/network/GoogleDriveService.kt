package com.demo.drive.data.network

import com.demo.drive.data.model.ListResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

internal interface GoogleDriveService {
    @GET("files")
    suspend fun list(
        @Query("q") q: String? = null,
        @Query("fields") fields: String? = null,
        @Query("orderBy") orderBy: String? = "folder,modifiedTime desc",
        @Query("pageToken") pageToken: String? = null,
        @Query("pageSize") pageSize: Int = 100,
    ): ListResponseDTO
}