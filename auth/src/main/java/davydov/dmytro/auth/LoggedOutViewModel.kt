package davydov.dmytro.auth

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import davydov.dmytro.core.BaseViewModel
import davydov.dmytro.core.Event
import davydov.dmytro.core.sendEvent
import davydov.dmytro.tokens.Tokens
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.SingleSubject

class LoggedOutViewModel(
    private val oAuthInteractor: OAuthInteractor,
    private val oAuthUrlCallbackParser: OAuthUrlCallbackParser,
    private val resources: Resources
) : BaseViewModel(), OAuthInteractor.Listener {

    private val _showAuthPopUp = MutableLiveData<AuthPopupState>()
    val showAuthPopUp: LiveData<AuthPopupState> = _showAuthPopUp

    private val _showAuthError = MutableLiveData<Event<String>>()
    val showAuthError: LiveData<Event<String>> = _showAuthError

    private var authSubject = SingleSubject.create<OAuthInteractor.AuthResult>()

    override fun auth(oAuthUrl: String): Single<OAuthInteractor.AuthResult> {
        authSubject = SingleSubject.create()

        _showAuthPopUp.value = AuthPopupState.Shown(oAuthUrl)

        return authSubject
    }

    override fun showAuthError() {
        _showAuthError.sendEvent(resources.getString(R.string.auth_error))
    }

    fun onLogInClick() {
        oAuthInteractor.auth()
    }

    fun onWebViewPageLoaded(url: String) {
        if (url.contains(CALLBACK_URL)) {
            _showAuthPopUp.value =
                AuthPopupState.Hidden

            val tokensWithUsername: Pair<Tokens, String>? =
                oAuthUrlCallbackParser.parseCallback(url)

            authSubject.onSuccess(
                OAuthInteractor.AuthResult(
                    tokensWithUsername?.first,
                    tokensWithUsername?.second
                )
            )
        }
    }

    fun shouldOverrideUrlLoading(url: String): Boolean {
        return url.contains(CALLBACK_URL)
    }

    fun onWebViewError() {
        _showAuthPopUp.value =
            AuthPopupState.Hidden
        authSubject.onError(Throwable())
    }

    sealed class AuthPopupState {
        data class Shown(val url: String) : AuthPopupState()
        object Hidden : AuthPopupState()
    }

    companion object {
        private const val CALLBACK_URL = "callback"
    }
}
