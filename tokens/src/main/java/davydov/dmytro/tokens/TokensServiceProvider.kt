package davydov.dmytro.tokens


interface TokensServiceProvider {
    fun provideTokensService(): TokensService
}