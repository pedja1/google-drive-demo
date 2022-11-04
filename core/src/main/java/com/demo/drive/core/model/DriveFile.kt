package com.demo.drive.core.model

data class DriveFile(
    val kind: String?,
    val name: String?,
    val id: String?,
    val mimeType: String?,
    val thumbnailLink: String?,
    val modifiedTime: String?,
    val size: Long?
)