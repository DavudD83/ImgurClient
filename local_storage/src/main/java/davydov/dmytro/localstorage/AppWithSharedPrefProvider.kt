package davydov.dmytro.localstorage


interface AppWithSharedPrefProvider {
    fun sharedPrefProvider(): SharedPreferencesProvider
}