package com.demo.drive.core.usecase.drive

import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.model.Result

interface DownloadFileUseCase {
    suspend fun downloadFile(driveFile: DriveFile): Result<Long>
}