package davydov.dmytro.imgurclient.root.loggedOut

import android.content.res.Resources
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.imgurclient.base.Injector
import davydov.dmytro.imgurclient.root.RootComponent
import javax.inject.Scope


@Component(dependencies = [RootComponent::class], modules = [LoggedOutModule::class])
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