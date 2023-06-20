package com.diningcoach.domain.model

import android.net.Uri
import java.util.Date

data class Photo(
    val uri: Uri,
    val name: String,
    val fullName: String,
    val mimeType: String,
    val addedDate: Date,
    val folder: String,
    val size: Long,
    val width: Int,
    val height: Int,
)
