package davydov.dmytro.core_api.routers

import androidx.fragment.app.FragmentTransaction

interface GalleryDetailsRouter {
    fun navigateToGalleryDetails(containerId: Int, transaction: FragmentTransaction, galleryId: String, galleryName: String)
}