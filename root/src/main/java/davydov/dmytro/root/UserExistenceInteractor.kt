package davydov.dmytro.root

import davydov.dmytro.tokens.TokensService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


@RootScope
class UserExistenceInteractor @Inject constructor(
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