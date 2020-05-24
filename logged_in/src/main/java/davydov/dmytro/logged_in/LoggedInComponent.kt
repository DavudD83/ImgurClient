package davydov.dmytro.logged_in

import dagger.Component
import davydov.dmytro.core_api.Injector
import javax.inject.Scope


@Component
@LoggedInScope
interface LoggedInComponent : Injector<LoggedInFragment>

@Scope
annotation class LoggedInScope