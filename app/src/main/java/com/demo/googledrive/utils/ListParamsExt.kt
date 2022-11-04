package com.demo.googledrive.utils

import com.demo.drive.core.model.OrderByDirection
import com.demo.drive.core.model.listParams

fun buildFolderListParams(folderId: String) = listParams {
    query {
        "'$folderId'" isIn "parents"
    }
    orderBy {
        field("folder")
        field("modifiedTime", OrderByDirection.Descending)
    }
    fields {
        field("nextPageToken")
        field("files") {
            field("thumbnailLink")
            field("kind")
            field("name")
            field("id")
            field("mimeType")
            field("modifiedTime")
            field("size")
        }
    }
}

fun buildSearchListParams(query: String) = listParams {
    query {
        "fullText" contains "'$query'"
    }
    fields {
        field("nextPageToken")
        field("files") {
            field("thumbnailLink")
            field("kind")
            field("name")
            field("id")
            field("mimeType")
            field("modifiedTime")
            field("size")
        }
    }
}