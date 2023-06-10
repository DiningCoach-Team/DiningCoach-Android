package com.diningcoach.domain.usecase.gallery

import androidx.paging.PagingSource
import com.diningcoach.domain.model.Gallery
import com.diningcoach.domain.repository.GalleryRepository
import javax.inject.Inject

class GalleryImageFetchUseCase @Inject constructor(
    private val repository: GalleryRepository
) {
    operator fun invoke(): PagingSource<Int, Gallery> =repository.getGalleryImagePagingSource()
}