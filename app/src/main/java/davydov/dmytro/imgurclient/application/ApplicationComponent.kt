package davydov.dmytro.imgurclient.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(modules = [AppModule::class])
@AppScope
interface ApplicationComponent {

    fun inject(app: ImgurClientApplication)

    fun objectMapper(): ObjectMapper
    fun sharedPref(): SharedPreferences
    fun context(): Context

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(@AppScope context: Context): Builder
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
    fun sharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences("ImgurClientPreferences", Context.MODE_PRIVATE)
    }
}

@Scope
annotation class AppScope