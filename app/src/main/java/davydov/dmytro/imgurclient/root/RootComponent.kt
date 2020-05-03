package davydov.dmytro.imgurclient.root

import android.content.Context
import android.content.res.Resources
import dagger.Component
import dagger.Module
import dagger.Provides
import davydov.dmytro.imgurclient.application.ApplicationComponent
import davydov.dmytro.imgurclient.base.Injector
import davydov.dmytro.imgurclient.root.tokensService.TokensService
import javax.inject.Scope

@Component(modules = [RootModule::class], dependencies = [ApplicationComponent::class])
@RootScope
interface RootComponent : Injector<RootFragment> {
    fun tokensService(): TokensService
    fun resources(): Resources
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

interface RootProvider {
    fun rootComponent(): RootComponent
}