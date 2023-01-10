package davydov.dmytro.logged_in

import android.content.Context
import com.example.network.*
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import retrofit2.Retrofit
import javax.inject.Scope


@Component(
    modules = [NetworkModule::class],
    dependencies = [ProvidersFacade::class, RetrofitProvider::class]
)
@LoggedInScope
interface LoggedInComponent : Injector<LoggedInFragment>, GalleriesApiProvider,
    ConnectivityServiceProvider

@Module
class NetworkModule {

    @Provides
    @LoggedInScope
    fun connectivityStateService(context: Context): ConnectionStateService =
        ConnectionStateService.create(context)

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