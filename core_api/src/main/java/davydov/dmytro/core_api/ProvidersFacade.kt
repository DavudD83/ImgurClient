package davydov.dmytro.core_api

import davydov.dmytro.core_api.routers.RoutersProvider


interface ProvidersFacade : ContextProvider,
    ResourcesProvider,
    ObjectMapperProvider,
    RoutersProvider,
    DispatchersProvider