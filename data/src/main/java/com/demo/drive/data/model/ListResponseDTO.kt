package com.demo.drive.data.model

import com.google.gson.annotations.SerializedName

internal data class ListResponseDTO(

    @field:SerializedName("kind")
	val kind: String? = null,

    @field:SerializedName("nextPageToken")
	val nextPageToken: String? = null,

    @field:SerializedName("files")
	val files: List<FilesItemDTO>? = null,

    @field:SerializedName("incompleteSearch")
	val incompleteSearch: Boolean? = null
)

internal data class FilesItemDTO(

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("mimeType")
	val mimeType: String? = null,

	@field:SerializedName("thumbnailLink")
	val thumbnailLink: String? = null,

	@field:SerializedName("modifiedTime")
	val modifiedTime: String? = null,

	@field:SerializedName("size")
	val size: Long? = null
)
