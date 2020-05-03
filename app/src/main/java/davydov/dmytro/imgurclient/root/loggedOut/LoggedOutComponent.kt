package davydov.dmytro.imgurclient.root.loggedOut

import dagger.Component
import davydov.dmytro.imgurclient.base.Injector
import davydov.dmytro.imgurclient.root.RootComponent
import javax.inject.Scope


@Component(dependencies = [RootComponent::class])
@LoggedOutScope
interface LoggedOutComponent : Injector<LoggedOutFragment>

@Scope
annotation class LoggedOutScope