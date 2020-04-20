package davydov.dmytro.imgurclient.root.loggedIn

import dagger.Component
import davydov.dmytro.imgurclient.base.Injector
import davydov.dmytro.imgurclient.root.RootComponent
import javax.inject.Scope


@Component(dependencies = [RootComponent::class])
@LoggedInScope
interface LoggedInComponent : Injector<LoggedInFragment>

@Scope
annotation class LoggedInScope