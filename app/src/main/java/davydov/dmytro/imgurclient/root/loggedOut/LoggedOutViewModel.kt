package davydov.dmytro.imgurclient.root.loggedOut

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.base.BaseViewModel
import davydov.dmytro.imgurclient.base.Event
import davydov.dmytro.imgurclient.base.Optional
import davydov.dmytro.imgurclient.base.sendEvent
import davydov.dmytro.imgurclient.root.tokensService.Tokens
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

    private var authSubject = SingleSubject.create<Optional<Tokens>>()

    override fun auth(oAuthUrl: String): Single<Optional<Tokens>> {
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
            _showAuthPopUp.value = AuthPopupState.Hidden

            val tokens = oAuthUrlCallbackParser.parseCallback(url)

            authSubject.onSuccess(Optional(tokens))
        }
    }

    fun shouldOverrideUrlLoading(url: String): Boolean {
        return url.contains(CALLBACK_URL)
    }

    fun onWebViewError() {
        _showAuthPopUp.value = AuthPopupState.Hidden
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
