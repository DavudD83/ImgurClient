package com.example.galleries

import androidx.fragment.app.FragmentManager
import com.example.galleries.ui.GalleriesFragment
import davydov.dmytro.core_api.routers.ViralGalleriesRouter
import javax.inject.Inject

class ViralGalleriesRouterImpl @Inject constructor() : ViralGalleriesRouter {
    override fun moveToViralGalleries(container: Int, fragmentManager: FragmentManager) {
        fragmentManager
            .beginTransaction()
            .add(container, GalleriesFragment())
            .commit()
    }
}