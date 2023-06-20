package com.diningcoach.data.repository.gallery.local

import android.net.Uri
import android.provider.MediaStore.Images.Media
import com.diningcoach.data.di.manager.local.MediaStoreManager
import com.diningcoach.data.model.PhotoModel
import javax.inject.Inject

class GalleryLocalDataSourceImpl @Inject constructor(
    private val mediaStoreManager: MediaStoreManager
) : GalleryLocalDataSource {

    override fun fetchGalleryImages(limit: Int, offset: Int): List<PhotoModel> {
        val contentUri = Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            Media._ID,
            Media.TITLE,
            Media.DISPLAY_NAME,
            Media.MIME_TYPE,
            Media.DATE_TAKEN,
            Media.BUCKET_DISPLAY_NAME,
            Media.SIZE,
            Media.WIDTH,
            Media.HEIGHT,
        )
        val galleryImage = mutableListOf<PhotoModel>()
        mediaStoreManager.fetchImages(projection, limit, offset)?.use{ cursor ->
            while (cursor.moveToNext()) {
                galleryImage.add(
                    PhotoModel(
                        uri = Uri.withAppendedPath(
                            contentUri,
                            cursor.getLong(cursor.getColumnIndexOrThrow(Media._ID)).toString()
                        ),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(Media.TITLE)),
                        fullName = cursor.getString(cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME)),
                        mimeType = cursor.getString(cursor.getColumnIndexOrThrow(Media.MIME_TYPE)),
                        addedDate = cursor.getLong(cursor.getColumnIndexOrThrow(Media.DATE_TAKEN)),
                        folder = cursor.getString(cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME)),
                        size = cursor.getLong(cursor.getColumnIndexOrThrow(Media.SIZE)),
                        width = cursor.getInt(cursor.getColumnIndexOrThrow(Media.WIDTH)),
                        height = cursor.getInt(cursor.getColumnIndexOrThrow(Media.HEIGHT)),
                    )
                )
            }
        }
        return galleryImage
    }
}