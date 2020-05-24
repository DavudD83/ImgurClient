package davydov.dmytro.tokens

import davydov.dmytro.core.Optional
import io.reactivex.rxjava3.core.Flowable


interface TokensService {
    fun getTokens(): Flowable<Optional<Tokens>>
    fun saveTokens(tokens: Tokens?)
}