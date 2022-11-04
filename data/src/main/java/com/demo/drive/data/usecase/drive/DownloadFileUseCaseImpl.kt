package com.demo.drive.data.usecase.drive

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.model.Result
import com.demo.drive.core.usecase.drive.DownloadFileUseCase
import com.demo.drive.data.Constants
import com.demo.drive.core.network.AuthorizationProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class DownloadFileUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authorizationProvider: AuthorizationProvider
) : DownloadFileUseCase {
    override suspend fun downloadFile(driveFile: DriveFile): Result<Long> {
        return when (val authorization = authorizationProvider.getAuthorizationHeader()) {
            is Result.Error -> Result.Error(authorization.exception)
            is Result.Success -> {
                val downloadManager =
                    ContextCompat.getSystemService(context, DownloadManager::class.java)
                if (downloadManager == null) {
                    Result.Error(NullPointerException("DownloadManager is null"))
                } else {
                    val downloadUri: Uri =
                        Uri.parse("${Constants.DRIVE_API_BASE_URL}files/${driveFile.id}?alt=media")
                    val request = DownloadManager.Request(downloadUri)
                    request.setTitle(driveFile.name)
                    request.addRequestHeader("Authorization", authorization.data)
                    request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        driveFile.name
                    )

                    val downloadId = downloadManager.enqueue(request)
                    val cursor: Cursor? =
                        downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

                    if (cursor != null && cursor.moveToNext()) {
                        val status: Int =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        val reason: String =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                        cursor.close()
                        if (status == DownloadManager.STATUS_FAILED) {
                            Result.Error(Exception(reason))
                        } else {
                            Result.Success(downloadId)
                        }
                    } else {
                        Result.Error(NullPointerException("cant get download status"))
                    }
                }
            }

        }

    }
}