package com.example.galleries.ui

import com.example.galleries.logic.Gallery
import org.mockito.ArgumentMatcher

class PictureUrlMatcher(galleries: List<Gallery>) : ArgumentMatcher<List<GalleryListItem>> {

    private val expectedPictures = galleries.map { it.images.first().url }

    override fun matches(argument: List<GalleryListItem>): Boolean {
        val resultPictures = argument.filterIsInstance<GalleryItem>().map { it.pictureUrl }

        return resultPictures == expectedPictures
    }
}