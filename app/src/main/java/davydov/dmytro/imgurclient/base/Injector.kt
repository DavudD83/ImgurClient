package davydov.dmytro.imgurclient.base


interface Injector<T> {
    fun inject(instance: T)
}