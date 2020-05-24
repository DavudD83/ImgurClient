package davydov.dmytro.tokens


interface WithTokensProvider {
    fun provider(): TokensServiceProvider
}