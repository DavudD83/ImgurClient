package davydov.dmytro.imgurclient.root

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class UserExistenceInteractor(
    private val tokensService: TokensService
) {
    lateinit var listener: Listener

    fun start() {
        tokensService
            .getTokens()
            .map { it.value != null }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tokensExist ->
                if (tokensExist) {
                    listener.navigateToLoggedIn()
                } else {
                    listener.navigateToLoggedOut()
                }
            }
    }

    interface Listener {
        fun navigateToLoggedIn()
        fun navigateToLoggedOut()
    }
}