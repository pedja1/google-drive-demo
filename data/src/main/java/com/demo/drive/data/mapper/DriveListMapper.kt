package com.demo.drive.data.mapper

import com.demo.drive.core.model.DriveFile
import com.demo.drive.data.model.ListResponseDTO

internal fun ListResponseDTO?.toDriveFileList(): List<DriveFile> {
    return this?.files?.map { filesItemDto ->
        DriveFile(
            filesItemDto.kind,
            filesItemDto.name,
            filesItemDto.id,
            filesItemDto.mimeType,
            filesItemDto.thumbnailLink,
            filesItemDto.modifiedTime,
            filesItemDto.size
        )
    } ?: emptyList()
}