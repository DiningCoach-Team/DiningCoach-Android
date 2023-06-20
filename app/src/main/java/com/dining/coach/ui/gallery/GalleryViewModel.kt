package com.dining.coach.ui.gallery

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dining.coach.base.BaseViewModel
import com.diningcoach.domain.usecase.gallery.GalleryImageFetchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryImageFetch: GalleryImageFetchUseCase
): BaseViewModel() {
    val galleryPager = Pager(
        config = PagingConfig(pageSize = 50)
    ) {
        galleryImageFetch()
    }.flow.cachedIn(viewModelScope)


}