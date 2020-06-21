package com.example.galleries.data

import com.example.galleries.di.GalleriesScope
import com.example.galleries.logic.Gallery
import com.example.galleries.logic.Image
import com.example.network.model.GalleriesResponse
import javax.inject.Inject

@GalleriesScope
class GalleriesMapper @Inject constructor() {
    fun mapToDomain(response: GalleriesResponse): List<Gallery> {
        return response
            .data
            .filter { it.images != null }
            .run { take((size / 2) * 2) }
            .map { remoteGallery ->
                val images = remoteGallery.images!!.map {
                    Image(
                        it.url,
                        it.id
                    )
                }

                remoteGallery.run {
                    Gallery(
                        id,
                        title,
                        views,
                        ups,
                        downs,
                        commentCount,
                        images
                    )
                }
            }
    }
}