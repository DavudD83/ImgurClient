package com.example.base_galleries

import com.example.network.model.GalleriesResponse
import com.example.network.model.RemoteGallery
import java.util.*
import java.util.concurrent.TimeUnit


class GalleriesMapper {
    fun mapToDomain(response: GalleriesResponse): List<Gallery> {
        return response
            .data
            .filter { it.images != null }
            .run { take((size / 2) * 2) }
            .map { remoteGallery -> mapToDomain(remoteGallery) }
    }

    fun mapToDomain(remoteGallery: RemoteGallery): Gallery {
        val images = remoteGallery.images!!.map {
            Image(
                it.url,
                it.id
            )
        }

        return remoteGallery.run {
            Gallery(
                id,
                title,
                views,
                ups,
                downs,
                commentCount,
                images,
                Date(TimeUnit.SECONDS.toMillis(datetimeSeconds))
            )
        }
    }
}