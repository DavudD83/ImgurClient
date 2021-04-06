package com.example.galleries

import com.example.galleries.logic.Gallery
import com.example.galleries.logic.Image

fun provideGalleries(): List<Gallery> {
    fun provideGallery(id: String): Gallery {
        return Gallery(
            id,
            "title",
            999,
            999,
            999,
            1000,
            provideImages()
        )
    }

    return ('a'..'f').map { provideGallery(it.toString()) }
}

fun provideImages(): List<Image> {
    return ('a'..'f').map { it.toString() }.map { Image(it, it) }
}