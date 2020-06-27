package davydov.dmytro.imgurclient

import android.content.Context
import android.content.res.Resources
import com.example.galleries.ViralGalleriesRouterImpl
import com.example.gallerydetails.GalleryDetailsRouterImpl
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.*
import davydov.dmytro.auth.LoggedOutRouterImpl
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import davydov.dmytro.core_api.routers.GalleryDetailsRouter
import davydov.dmytro.core_api.routers.LoggedInRouter
import davydov.dmytro.core_api.routers.LoggedOutRouter
import davydov.dmytro.core_api.routers.ViralGalleriesRouter
import davydov.dmytro.localstorage.SharedPreferencesProvider
import davydov.dmytro.logged_in.LoggedInRouterImpl
import javax.inject.Scope

@Component(modules = [AppModule::class, RoutersBindings::class], dependencies = [SharedPreferencesProvider::class])
@AppScope
interface ApplicationComponent : Injector<ImgurClientApplication>, ProvidersFacade, SharedPreferencesProvider {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(@AppScope context: Context): Builder
        fun sharedPrefProvider(sharedPreferencesProvider: SharedPreferencesProvider): Builder
        fun build(): ApplicationComponent
    }
}

@Module
class AppModule {
    @Provides
    @AppScope
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
    }

    @Provides
    @AppScope
    fun resources(context: Context): Resources = context.resources
}

@Module
abstract class RoutersBindings {
    @Binds
    @AppScope
    abstract fun loggedOutRouter(impl: LoggedOutRouterImpl): LoggedOutRouter

    @Binds
    @AppScope
    abstract fun loggedInRouter(impl: LoggedInRouterImpl): LoggedInRouter

    @Binds
    @AppScope
    abstract fun viralGalleriesRouter(impl: ViralGalleriesRouterImpl): ViralGalleriesRouter

    @Binds
    @AppScope
    abstract fun galleryDetailsRouter(impl: GalleryDetailsRouterImpl): GalleryDetailsRouter
}

@Scope
annotation class AppScope