package com.demo.drive.data.model

import com.google.gson.annotations.SerializedName

internal data class ErrorResponseDTO(
    @field:SerializedName("error")
    val error: Error?
)

internal data class Error(
    @field:SerializedName("code")
    val code: Int?,
    @field:SerializedName("message")
    val message: String?
)