package davydov.dmytro.feature_account.di

import com.example.network.ConnectivityServiceProvider
import com.example.network.RetrofitProvider
import dagger.Component
import davydov.dmytro.core_api.ProvidersFacade
import davydov.dmytro.data_user.di.UserDataModule
import davydov.dmytro.feature_account.posts.di.PostsDataModule
import davydov.dmytro.feature_account.posts.ui.PostsFragment
import davydov.dmytro.feature_account.ui.AccountFragment
import davydov.dmytro.localstorage.SharedPreferencesProvider

@Component(
    dependencies = [
        ProvidersFacade::class,
        RetrofitProvider::class,
        SharedPreferencesProvider::class,
        ConnectivityServiceProvider::class],
    modules = [UserDataModule::class, PostsDataModule::class]
)
interface AccountComponent {

    fun inject(fragment: AccountFragment)
    fun inject(fragment: PostsFragment)

    @Component.Factory
    interface Factory {
        fun build(
            providersFacade: ProvidersFacade,
            retrofitProvider: RetrofitProvider,
            sharedPreferencesProvider: SharedPreferencesProvider,
            connectivityServiceProvider: ConnectivityServiceProvider
        ): AccountComponent
    }
}