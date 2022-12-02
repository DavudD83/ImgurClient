package davydov.dmytro.auth

import davydov.dmytro.data_user.domain.UserRepository
import davydov.dmytro.tokens.Tokens
import davydov.dmytro.tokens.TokensService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


@LoggedOutScope
class OAuthInteractor @Inject constructor(
    private val tokensService: TokensService,
    private val oAuthUrlFactory: OAuthUrlFactory,
    private val userRepository: UserRepository
) {
    lateinit var listener: Listener

    fun auth() {
        listener
            .auth(oAuthUrlFactory.createUrl())
            .subscribe(
                { (tokens, userName) ->
                    if (tokens != null && userName != null) {
                        tokensService.saveTokens(tokens)
                        userRepository.saveUserName(userName)
                    } else {
                        listener.showAuthError()
                    }
                },
                { listener.showAuthError() }
            )
    }

    interface Listener {
        fun auth(oAuthUrl: String): Single<AuthResult>
        fun showAuthError()
    }

    data class AuthResult(val tokens: Tokens?, val userName: String?)
}