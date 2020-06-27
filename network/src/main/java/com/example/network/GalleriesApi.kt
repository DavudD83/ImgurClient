package com.example.network

import com.example.network.model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GalleriesApi {
    @GET("{section}/{sort}/{window}/{page}")
    fun loadGalleries(
        @Path("section") section: GallerySection = GallerySection.HOT,
        @Path("sort") sort: GallerySort = GallerySort.VIRAL,
        @Path("window") window: GalleryWindow = GalleryWindow.WEEK,
        @Path("page") page: Int = 0,
        @Query("showViral") showViral: Boolean = true
    ): Single<GalleriesResponse>

    @GET("album/{galleryId}")
    fun loadGalleryAlbum(@Path("galleryId") id: String): Single<GalleryResponse>
}