package davydov.dmytro.imgurclient.root

import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.imgurclient.application.ApplicationComponent
import davydov.dmytro.imgurclient.base.Injector
import javax.inject.Scope

@Component(modules = [RootModule::class], dependencies = [ApplicationComponent::class])
@RootScope
interface RootComponent : Injector<RootFragment>

@Module
class RootModule {

    @Provides
    @RootScope
    fun rootVM(userExistenceInteractor: UserExistenceInteractor): RootViewModel {
        val rootVm = RootViewModel(userExistenceInteractor)
        userExistenceInteractor.listener = rootVm

        return rootVm
    }
}

@Scope
annotation class RootScope

interface RootProvider {
    fun rootComponent(): RootComponent
}