package davydov.dmytro.logged_in

import android.util.Log
import com.example.network.GalleriesApi
import com.example.network.GalleriesApiProvider
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import davydov.dmytro.tokens.TokensServiceProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Scope


@Component(modules = [NetworkModule::class], dependencies = [ProvidersFacade::class, TokensServiceProvider::class])
@LoggedInScope
interface LoggedInComponent : Injector<LoggedInFragment>, GalleriesApiProvider

@Module
class NetworkModule {

    @Provides
    @LoggedInScope
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("HTTP", message)
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @LoggedInScope
    fun provideRetrofit(client: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.imgur.com/3/")
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @LoggedInScope
    fun provideGalleriesApi(retrofit: Retrofit): GalleriesApi {
        return retrofit.newBuilder()
            .baseUrl(
                retrofit
                    .baseUrl()
                    .newBuilder()
                    .addPathSegments("gallery/")
                    .build()
            )
            .build()
            .create(GalleriesApi::class.java)
    }
}

@Scope
annotation class LoggedInScope