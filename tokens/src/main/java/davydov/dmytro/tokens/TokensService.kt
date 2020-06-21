package davydov.dmytro.tokens

import davydov.dmytro.core.Optional
import io.reactivex.rxjava3.core.Flowable


interface TokensService {
    val peekTokens: Tokens?
    fun getTokens(): Flowable<Optional<Tokens>>
    fun saveTokens(tokens: Tokens?)
}