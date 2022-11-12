package davydov.dmytro.localstorage

import android.content.Context
import android.content.SharedPreferences
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [LocalStorageModule::class])
@Singleton
interface LocalStorageComponent : SharedPreferencesProvider {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): LocalStorageComponent
    }
}

object LocalStorageComponentFactory {
    fun create(context: Context): LocalStorageComponent {
        return DaggerLocalStorageComponent.factory().create(context)
    }
}

@Module
class LocalStorageModule {

    @Provides
    @Singleton
    fun sharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val SHARED_PREF_NAME = "ImgurClientPreferences"
    }
}