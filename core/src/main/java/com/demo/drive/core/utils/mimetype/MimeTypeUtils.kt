package com.demo.drive.core.utils.mimetype

private val DOCUMENT_SUFFIXES = arrayOf(
    "msword",
    "vnd.openxmlformats-officedocument.wordprocessingml.document",
    "vnd.openxmlformats-officedocument.wordprocessingml.template",
    "vnd.ms-word.document.macroEnabled.12",
    "vnd.ms-word.template.macroEnabled.12",
    "vnd.ms-excel",
    "vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    "vnd.openxmlformats-officedocument.spreadsheetml.template",
    "vnd.ms-excel.sheet.macroEnabled.12",
    "vnd.ms-excel.template.macroEnabled.12",
    "vnd.ms-excel.addin.macroEnabled.12",
    "vnd.ms-excel.sheet.binary.macroEnabled.12",
    "vnd.ms-powerpoint",
    "vnd.openxmlformats-officedocument.presentationml.presentation",
    "vnd.openxmlformats-officedocument.presentationml.template",
    "vnd.openxmlformats-officedocument.presentationml.slideshow",
    "vnd.ms-powerpoint.addin.macroEnabled.12",
    "vnd.ms-powerpoint.presentation.macroEnabled.12",
    "vnd.ms-powerpoint.template.macroEnabled.12",
    "vnd.ms-powerpoint.slideshow.macroEnabled.12",
    "vnd.ms-access",
    "x-abiword",
    "vnd.oasis.opendocument.presentation",
    "vnd.oasis.opendocument.spreadsheet",
    "vnd.oasis.opendocument.text",
    "vnd.visio",
    "pdf",
)

private val ARCHIVE_SUFFIXES = arrayOf(
    "x-archive",
    "x-cpio",
    "x-shar",
    "x-iso9660-image",
    "x-sbx",
    "x-tar",
    "x-bzip2",
    "gzip",
    "x-lzip",
    "x-lzma",
    "x-lzop",
    "x-snappy-framed",
    "x-xz",
    "x-compress",
    "zstd",
    "x-7z-compressed",
    "x-ace-compressed",
    "x-astrotite-afa",
    "x-alz-compressed",
    "vnd.android.package-archive",
    "octet-stream",
    "x-freearc",
    "x-arj",
    "x-b1",
    "vnd.ms-cab-compressed",
    "x-cfs-compressed",
    "x-dar",
    "x-dgc-compressed",
    "x-apple-diskimage",
    "x-gca-compressed",
    "java-archive",
    "x-lzh",
    "x-lzx",
    "x-rar-compressed",
    "x-stuffit",
    "x-stuffitx",
    "x-gtar",
    "x-ms-wim",
    "x-xar",
    "zip",
    "x-zoo",
    "x-par2",
)

private fun String?.getPrefix(): String? {
    return this?.split("/")?.firstOrNull()
}

private fun String?.getSuffix(): String? {
    return this?.split("/")?.lastOrNull()
}

fun String?.isImage(): Boolean {
    this ?: return false
    return getPrefix() == "image"
}

fun String?.isAudio(): Boolean {
    this ?: return false
    return getPrefix() == "audio"
}

fun String?.isVideo(): Boolean {
    this ?: return false
    return getPrefix() == "video"
}

fun String?.isText(): Boolean {
    this ?: return false
    return getPrefix() == "text"
}

fun String?.isFont(): Boolean {
    this ?: return false
    return getPrefix() == "font"
}

fun String?.isArchive(): Boolean {
    this ?: return false
    return getPrefix() == "application" && ARCHIVE_SUFFIXES.contains(getSuffix())
}

fun String?.isDocument(): Boolean {
    this ?: return false
    return getPrefix() == "application" && DOCUMENT_SUFFIXES.contains(getSuffix())
}

fun String?.isFolder(): Boolean {
    this ?: return false
    return getPrefix() == "application" && getSuffix() == "vnd.google-apps.folder"
}

fun String?.toFileType(): FileType {
    return when {
        isImage() -> FileType.IMAGE
        isAudio() -> FileType.AUDIO
        isVideo() -> FileType.VIDEO
        isText() -> FileType.TEXT
        isArchive() -> FileType.ARCHIVE
        isDocument() -> FileType.DOCUMENT
        isFont() -> FileType.FONT
        isFolder() -> FileType.FOLDER
        else -> FileType.OTHER
    }
}