package davydov.dmytro.core_api.routers


interface RoutersProvider {
    fun provideLoggedOutRouter(): LoggedOutRouter
    fun provideLoggedInRouter(): LoggedInRouter
    fun provideViralGalleriesRouter(): ViralGalleriesRouter
}