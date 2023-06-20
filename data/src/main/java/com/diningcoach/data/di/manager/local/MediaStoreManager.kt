package com.diningcoach.data.di.manager.local

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaStoreManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val contentResolver = context.contentResolver

    fun fetchImages(projection:Array<String>, limit: Int, offset: Int): Cursor?{
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.SIZE + " > 0"
            else null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return contentResolver.query(
                contentUri,
                projection,
                Bundle().apply {
                    // Limit & Offset
                    putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)

                    // Sort function
                    putStringArray(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        arrayOf(MediaStore.Images.Media.DATE_TAKEN)
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
                "${MediaStore.Images.Media.DATE_TAKEN} DESC, ${MediaStore.Images.Media._ID} DESC LIMIT $limit OFFSET $offset"
            return contentResolver.query(
                contentUri,
                projection,
                selection,
                null,
                sortOrder
            )
        }
    }
}