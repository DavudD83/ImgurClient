package davydov.dmytro.core_api


interface Injector<T> {
    fun inject(instance: T)
}