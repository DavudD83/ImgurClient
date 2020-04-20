package davydov.dmytro.imgurclient.root.tokensService

import android.content.SharedPreferences
import com.fasterxml.jackson.databind.ObjectMapper
import davydov.dmytro.imgurclient.base.Optional
import davydov.dmytro.imgurclient.root.RootScope
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import javax.inject.Inject

@RootScope
class TokensService @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val objectMapper: ObjectMapper
) {
    private val tokens = BehaviorProcessor.createDefault(
        Optional(
            sharedPreferences.getString(KEY_TOKENS, null)
                ?.let { objectMapper.readValue(it, Tokens::class.java) }
        )
    )

    fun getTokens(): Flowable<Optional<Tokens>> = tokens

    companion object {
        private const val KEY_TOKENS = "KEY_TOKENS"
    }
}
