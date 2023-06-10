package com.diningcoach.data.model

import android.net.Uri

data class GalleryModel(
    val uri: Uri,
    val name: String,
    val fullName: String,
    val mimeType: String,
    val addedDate: Long,
    val folder: String,
    val size: Long,
    val width: Int,
    val height: Int,
)
