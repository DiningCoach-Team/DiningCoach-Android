package com.diningcoach.domain.repository

import androidx.paging.PagingSource
import com.diningcoach.domain.model.Gallery

interface GalleryRepository {
    fun getGalleryImagePagingSource(): PagingSource<Int, Gallery>
}