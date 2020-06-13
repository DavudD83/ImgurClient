package davydov.dmytro.imgurclient

import android.app.Application
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.core_api.ProvidersFacade
import davydov.dmytro.localstorage.AppWithSharedPrefProvider
import davydov.dmytro.localstorage.LocalStorageComponentFactory
import davydov.dmytro.localstorage.SharedPreferencesProvider
import timber.log.Timber


class ImgurClientApplication : Application(), AppWithFacade, AppWithSharedPrefProvider {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        DaggerApplicationComponent
            .builder()
            .context(this)
            .sharedPrefProvider(LocalStorageComponentFactory.create(this))
            .build()
            .also { appComponent = it }
            .inject(this)
    }

    override fun facade(): ProvidersFacade = appComponent

    override fun sharedPrefProvider(): SharedPreferencesProvider = appComponent

    companion object {
        lateinit var appComponent: ApplicationComponent
    }
}