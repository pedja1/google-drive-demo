package com.demo.googledrive.utils

import android.content.Context
import android.text.format.Formatter
import com.demo.drive.core.utils.mimetype.FileType
import com.demo.googledrive.R

fun FileType.getIcon(): Int {
    return when(this) {
        FileType.DOCUMENT,
        FileType.TEXT -> R.drawable.ic_file_type_document
        FileType.AUDIO -> R.drawable.ic_file_type_audio
        FileType.VIDEO -> R.drawable.ic_file_type_video
        FileType.IMAGE -> R.drawable.ic_file_type_photo
        FileType.ARCHIVE -> R.drawable.ic_file_type_archive
        FileType.FONT,
        FileType.OTHER -> R.drawable.ic_file_type_other
        FileType.FOLDER -> R.drawable.ic_file_type_folder
    }
}

fun Long?.toHumanReadableSize(context: Context): String? {
    this ?: return null
    return Formatter.formatFileSize(context, this)
}