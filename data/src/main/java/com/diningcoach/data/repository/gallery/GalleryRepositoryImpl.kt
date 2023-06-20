package com.diningcoach.data.repository.gallery

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.diningcoach.data.extensions.toPhoto
import com.diningcoach.data.repository.gallery.local.GalleryLocalDataSource
import com.diningcoach.domain.model.Photo
import com.diningcoach.domain.repository.GalleryRepository
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryLocalDataSource: GalleryLocalDataSource,
): GalleryRepository {
    override fun getGalleryImagePagingSource():PagingSource<Int, Photo> {
        return object: PagingSource<Int, Photo>() {
            override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
                try {
                    val pageNumber = params.key ?: 0
                    val response = galleryLocalDataSource.fetchGalleryImages(params.loadSize, pageNumber*params.loadSize)
                    return LoadResult.Page(
                        data = response.map { it.toPhoto() },
                        prevKey = if(pageNumber==0) null else pageNumber-1,
                        nextKey = if(response.isEmpty()) null else pageNumber+1
                    )
                } catch (e: Exception) {
                    // TODO("error process")
                    throw e
                }
            }
        }
    }
}