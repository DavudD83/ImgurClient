package davydov.dmytro.imgurclient

import android.content.Context
import android.content.res.Resources
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.*
import davydov.dmytro.auth.LoggedOutRouterImpl
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import davydov.dmytro.core_api.routers.LoggedInRouter
import davydov.dmytro.core_api.routers.LoggedOutRouter
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
}

@Scope
annotation class AppScope