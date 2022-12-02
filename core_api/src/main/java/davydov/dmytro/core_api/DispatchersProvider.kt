package davydov.dmytro.core_api

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher
}