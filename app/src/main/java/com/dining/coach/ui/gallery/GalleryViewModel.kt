package com.dining.coach.ui.gallery

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.dining.coach.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(

): BaseViewModel() {
    private val INDEX_ID = MediaStore.MediaColumns._ID
    private val INDEX_URI = MediaStore.MediaColumns.DATA
    private val INDEX_DATE = MediaStore.MediaColumns.DATE_ADDED

    private val _images = MutableStateFlow<List<Uri>>(emptyList())
    val images: StateFlow<List<Uri>>
        get() = _images

    suspend fun fetchImages(context: Context) = withContext(Dispatchers.IO){
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            INDEX_ID,
            INDEX_URI,
            INDEX_DATE
        )
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.SIZE + " > 0"
            else null
        val sortOrder = "$INDEX_DATE DESC"
        val cursor = context.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            sortOrder
        )
        cursor?.let {
            val result = mutableListOf<Uri>()
            val columnIndexID = cursor.getColumnIndexOrThrow(INDEX_ID)
            var imageId: Long
            while(cursor.moveToNext()) {
                imageId = cursor.getLong(columnIndexID)
                result.add(
                    Uri.withAppendedPath(uri, imageId.toString())
                )
            }
            _images.emit(result)
        }
        cursor?.close()
    }
}