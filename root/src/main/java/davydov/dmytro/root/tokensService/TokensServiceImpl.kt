package davydov.dmytro.root.tokensService

import android.content.SharedPreferences
import androidx.core.content.edit
import com.fasterxml.jackson.databind.ObjectMapper
import davydov.dmytro.core.Optional
import davydov.dmytro.root.RootScope
import davydov.dmytro.tokens.Tokens
import davydov.dmytro.tokens.TokensService
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import javax.inject.Inject

class TokensServiceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val objectMapper: ObjectMapper
) : TokensService {
    private val tokensProcessor = BehaviorProcessor.createDefault(
        Optional(
            sharedPreferences.getString(KEY_TOKENS, null)
                ?.let { objectMapper.readValue(it, Tokens::class.java) }
        )
    )

    override fun getTokens(): Flowable<Optional<Tokens>> = tokensProcessor

    override fun saveTokens(tokens: Tokens?) {
        sharedPreferences.edit {
            val tokensStr = objectMapper.writeValueAsString(tokens)
            putString(KEY_TOKENS, tokensStr)
        }
        tokensProcessor.onNext(Optional(tokens))
    }

    override val peekTokens: Tokens?
        get() = tokensProcessor.value?.value

    companion object {
        private const val KEY_TOKENS = "KEY_TOKENS"
    }
}
