package com.example.gallerydetails

import android.content.res.Resources
import com.example.base_galleries.GalleriesMapper
import com.example.network.GalleriesApiProvider
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import javax.inject.Qualifier
import javax.inject.Scope

@Component(
    dependencies = [ProvidersFacade::class, GalleriesApiProvider::class],
    modules = [GalleryModule::class]
)
@GalleryScope
abstract class GalleryComponent : Injector<FragmentGallery> {

    @Component.Factory
    abstract class Factory {
        abstract fun create(
            providersFacade: ProvidersFacade,
            galleriesApiProvider: GalleriesApiProvider,
            @BindsInstance @GalleryId galleryId: String,
            @BindsInstance @GalleryName name: String
        ): GalleryComponent
    }
}

@Module
class GalleryModule {
    @Provides
    @GalleryScope
    fun mapper(): GalleriesMapper = GalleriesMapper()

    @Provides
    @GalleryScope
    fun provideVM(loadGalleryInteractor: LoadGalleryInteractor, resources: Resources, @GalleryName name: String): GalleryVM {
        val viewModel = GalleryVM(loadGalleryInteractor, resources, name)
        loadGalleryInteractor.listener = viewModel

        return viewModel
    }
}

@Scope
annotation class GalleryScope

@Qualifier
annotation class GalleryId

@Qualifier
annotation class GalleryName