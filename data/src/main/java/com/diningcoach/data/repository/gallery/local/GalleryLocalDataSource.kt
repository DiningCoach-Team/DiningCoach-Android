package com.diningcoach.data.repository.gallery.local

import com.diningcoach.data.model.GalleryModel

interface GalleryLocalDataSource {
    fun fetchGalleryImages(limit: Int, offset: Int): List<GalleryModel>
}