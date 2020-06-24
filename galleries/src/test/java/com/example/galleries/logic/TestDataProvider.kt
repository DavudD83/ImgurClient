package com.example.galleries.logic

fun provideGalleries(): List<Gallery> {
    fun provideGallery(id: String): Gallery {
        return Gallery(id, "title", 999, 999, 999, 1000, emptyList())
    }

    return ('a'..'f').map { provideGallery(it.toString()) }
}