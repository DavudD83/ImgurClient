package davydov.dmytro.root

import android.content.Context
import android.content.res.Resources
import com.example.network.RetrofitProvider
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ObjectMapperProvider
import davydov.dmytro.core_api.routers.RoutersProvider
import davydov.dmytro.localstorage.SharedPreferencesProvider
import davydov.dmytro.root.interceptors.ApiErrorInterceptor
import davydov.dmytro.root.interceptors.AuthInterceptor
import davydov.dmytro.root.interceptors.LoggedOutInterceptor
import davydov.dmytro.root.interceptors.NetworkErrorInterceptor
import davydov.dmytro.root.tokensService.TokensServiceImpl
import davydov.dmytro.tokens.TokensService
import davydov.dmytro.tokens.TokensServiceProvider
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Scope

@Component(
    modules = [RootModule::class, TokensServiceBinding::class, NetworkModule::class],
    dependencies = [RoutersProvider::class, SharedPreferencesProvider::class, ObjectMapperProvider::class]
)
@RootScope
interface RootComponent : Injector<RootFragment>, TokensServiceProvider, RetrofitProvider

@Module
interface TokensServiceBinding {
    @Binds
    @RootScope
    fun tokensService(impl: TokensServiceImpl): TokensService
}

@Module
class RootModule {

    @Provides
    @RootScope
    fun rootVM(userExistenceInteractor: UserExistenceInteractor): RootViewModel {
        val rootVm = RootViewModel(userExistenceInteractor)
        userExistenceInteractor.listener = rootVm

        return rootVm
    }

    @Provides
    @RootScope
    fun resources(context: Context): Resources = context.resources
}

@Module
class NetworkModule {

    companion object {
        private const val TIMEOUT_TIME = 30L
    }

    @Provides
    @RootScope
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
            .readTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @RootScope
    fun certificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add("*.imgur.com", "sha256/67qfENcnm/SGGFJOb5ENkUThQ3rIk+YR4u6zbYxRMWA=")
            .build()
    }

    @Provides
    @RootScope
    fun provideRetrofit(client: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.imgur.com/3/")
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}

@Scope
annotation class RootScope