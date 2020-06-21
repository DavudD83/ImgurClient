package com.example.galleries.di

import android.content.res.Resources
import com.example.galleries.logic.LoadGalleriesInteractor
import com.example.galleries.ui.GalleriesFragment
import com.example.galleries.ui.GalleriesViewModel
import com.example.network.GalleriesApiProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import javax.inject.Scope

@Component(dependencies = [ProvidersFacade::class, GalleriesApiProvider::class], modules = [GalleriesModule::class])
@GalleriesScope
interface GalleriesComponent : Injector<GalleriesFragment>

@Module
class GalleriesModule {
    @Provides
    @GalleriesScope
    fun provideVM(resources: Resources, loadGalleriesInteractor: LoadGalleriesInteractor): GalleriesViewModel {
        val viewModel = GalleriesViewModel(
            resources,
            loadGalleriesInteractor
        )
        loadGalleriesInteractor.listener = viewModel

        return viewModel
    }
}

@Scope
annotation class GalleriesScope