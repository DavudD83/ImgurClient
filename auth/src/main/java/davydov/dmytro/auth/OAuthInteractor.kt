package davydov.dmytro.auth

import davydov.dmytro.core.Optional
import davydov.dmytro.tokens.Tokens
import davydov.dmytro.tokens.TokensService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


@LoggedOutScope
class OAuthInteractor @Inject constructor(
    private val tokensService: TokensService,
    private val oAuthUrlFactory: OAuthUrlFactory
) {
    lateinit var listener: Listener

    fun auth() {
        listener
            .auth(oAuthUrlFactory.createUrl())
            .subscribe(
                { optionalTokens ->
                    val tokens = optionalTokens.value

                    if (tokens != null) {
                        tokensService.saveTokens(tokens)
                    } else {
                        listener.showAuthError()
                    }
                },
                { listener.showAuthError() }
            )
    }

    interface Listener {
        fun auth(oAuthUrl: String): Single<Optional<Tokens>>
        fun showAuthError()
    }
}