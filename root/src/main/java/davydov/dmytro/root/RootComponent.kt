package davydov.dmytro.root

import android.content.Context
import android.content.res.Resources
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ObjectMapperProvider
import davydov.dmytro.core_api.routers.RoutersProvider
import davydov.dmytro.localstorage.SharedPreferencesProvider
import davydov.dmytro.root.tokensService.TokensServiceImpl
import davydov.dmytro.tokens.TokensService
import davydov.dmytro.tokens.TokensServiceProvider
import javax.inject.Scope

@Component(modules = [RootModule::class, TokensServiceBinding::class], dependencies = [RoutersProvider::class, SharedPreferencesProvider::class, ObjectMapperProvider::class])
@RootScope
interface RootComponent : Injector<RootFragment>, TokensServiceProvider

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

@Scope
annotation class RootScope