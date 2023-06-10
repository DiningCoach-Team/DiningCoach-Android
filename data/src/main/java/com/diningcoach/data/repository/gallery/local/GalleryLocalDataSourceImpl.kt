package com.diningcoach.data.repository.gallery.local

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import com.diningcoach.data.model.GalleryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GalleryLocalDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context
) : GalleryLocalDataSource {
    private val contentResolver = context.contentResolver

    override fun fetchGalleryImages(limit: Int, offset: Int): List<GalleryModel> {
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
        val galleryImage = mutableListOf<GalleryModel>()
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Media.SIZE + " > 0"
            else null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.query(
                contentUri,
                projection,
                Bundle().apply {
                    // Limit & Offset
                    putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)

                    // Sort function
                    putStringArray(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        arrayOf(Media.DATE_TAKEN)
                    )
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                    )

                    // Selection
                    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                }, null
            )
        } else {
            val sortOrder =
                "${Media.DATE_TAKEN} DESC, ${Media._ID} DESC LIMIT $limit OFFSET $offset"
            contentResolver.query(
                contentUri,
                projection,
                selection,
                null,
                sortOrder
            )
        }?.use { cursor ->
            while (cursor.moveToNext()) {
                galleryImage.add(
                    GalleryModel(
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