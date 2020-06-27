package com.example.gallerydetails

import androidx.fragment.app.FragmentTransaction
import davydov.dmytro.core_api.routers.GalleryDetailsRouter
import javax.inject.Inject

class GalleryDetailsRouterImpl @Inject constructor() : GalleryDetailsRouter {
    override fun navigateToGalleryDetails(
        containerId: Int,
        transaction: FragmentTransaction,
        galleryId: String,
        galleryName: String
    ) {
        transaction
            .replace(containerId, FragmentGallery.create(galleryId, galleryName))
            .commit()
    }
}