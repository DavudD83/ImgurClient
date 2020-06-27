package com.example.gallerydetails

import com.example.base_galleries.GalleriesMapper
import com.example.base_galleries.Gallery
import com.example.network.GalleriesApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@GalleryScope
class SingleGalleryService @Inject constructor(
    private val api: GalleriesApi,
    private val galleriesMapper: GalleriesMapper
) {
    fun loadGalleryInfoBy(id: String): Single<Gallery> {
        return api
            .loadGalleryAlbum(id)
            .map { galleriesMapper.mapToDomain(it.data) }
    }
}