package com.demo.drive.data.network

import com.demo.drive.core.model.ListParams
import com.demo.drive.core.model.Result
import com.demo.drive.data.model.ListResponseDTO

internal interface DriveApi {
    suspend fun list(listParams: ListParams, pageToken: String?): Result<ListResponseDTO>
}