package davydov.dmytro.logged_in

import android.content.Context
import com.example.network.ConnectionStateService
import com.example.network.ConnectivityServiceProvider
import com.example.network.GalleriesApi
import com.example.network.GalleriesApiProvider
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import davydov.dmytro.logged_in.interceptors.ApiErrorInterceptor
import davydov.dmytro.logged_in.interceptors.AuthInterceptor
import davydov.dmytro.logged_in.interceptors.LoggedOutInterceptor
import davydov.dmytro.logged_in.interceptors.NetworkErrorInterceptor
import davydov.dmytro.tokens.TokensServiceProvider
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber
import javax.inject.Scope


@Component(
    modules = [NetworkModule::class],
    dependencies = [ProvidersFacade::class, TokensServiceProvider::class]
)
@LoggedInScope
interface LoggedInComponent : Injector<LoggedInFragment>, GalleriesApiProvider, ConnectivityServiceProvider

@Module
class NetworkModule {

    @Provides
    @LoggedInScope
    fun connectivityStateService(context: Context): ConnectionStateService = ConnectionStateService.create(context)

    @Provides
    @LoggedInScope
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggedOutInterceptor: LoggedOutInterceptor,
        networkErrorInterceptor: NetworkErrorInterceptor,
        apiErrorInterceptor: ApiErrorInterceptor,
        certificatePinner: CertificatePinner
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkErrorInterceptor)
            .addInterceptor(apiErrorInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggedOutInterceptor)
            .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Timber.tag("HTTP").d(message)
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .certificatePinner(certificatePinner)
            .build()
    }

    @Provides
    @LoggedInScope
    fun certificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add("*.imgur.com", "sha256/67qfENcnm/SGGFJOb5ENkUThQ3rIk+YR4u6zbYxRMWA=")
            .build()
    }

    @Provides
    @LoggedInScope
    fun provideRetrofit(client: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.imgur.com/3/")
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
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