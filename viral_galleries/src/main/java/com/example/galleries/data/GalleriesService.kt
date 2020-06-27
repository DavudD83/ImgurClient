package com.example.galleries.data

import com.example.base_galleries.GalleriesMapper
import com.example.base_galleries.Gallery
import com.example.galleries.di.GalleriesScope
import com.example.network.GalleriesApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@GalleriesScope
class GalleriesService @Inject constructor(
    private val galleriesApi: GalleriesApi,
    private val galleriesMapper: GalleriesMapper
) {
    fun loadViral(page: Int): Single<List<Gallery>> {
        return galleriesApi
            .loadGalleries(page = page)
            .map(galleriesMapper::mapToDomain)
    }
}