package davydov.dmytro.core_api.routers

import androidx.fragment.app.FragmentManager

interface ViralGalleriesRouter {
    fun moveToViralGalleries(container: Int, fragmentManager: FragmentManager)
}