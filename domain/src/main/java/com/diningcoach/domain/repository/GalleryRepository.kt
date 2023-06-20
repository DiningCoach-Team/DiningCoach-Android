package com.diningcoach.domain.repository

import androidx.paging.PagingSource
import com.diningcoach.domain.model.Photo

interface GalleryRepository {
    fun getGalleryImagePagingSource(): PagingSource<Int, Photo>
}