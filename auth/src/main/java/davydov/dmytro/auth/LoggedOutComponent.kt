package davydov.dmytro.auth

import android.content.res.Resources
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.core_api.Injector
import davydov.dmytro.core_api.ProvidersFacade
import davydov.dmytro.tokens.TokensServiceProvider
import javax.inject.Scope


@Component(dependencies = [TokensServiceProvider::class, ProvidersFacade::class], modules = [LoggedOutModule::class])
@LoggedOutScope
interface LoggedOutComponent : Injector<LoggedOutFragment>

@Module
class LoggedOutModule {
    @Provides
    @LoggedOutScope
    fun loggedOutVM(
        oAuthInteractor: OAuthInteractor,
        oAuthUrlCallbackParser: OAuthUrlCallbackParser,
        resources: Resources
    ): LoggedOutViewModel {
        val loggedOutViewModel =
            LoggedOutViewModel(oAuthInteractor, oAuthUrlCallbackParser, resources)
        oAuthInteractor.listener = loggedOutViewModel

        return loggedOutViewModel
    }
}

@Scope
annotation class LoggedOutScope