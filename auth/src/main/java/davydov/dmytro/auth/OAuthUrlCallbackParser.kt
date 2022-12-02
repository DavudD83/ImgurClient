package davydov.dmytro.auth

import android.net.Uri
import davydov.dmytro.tokens.Tokens
import javax.inject.Inject

@LoggedOutScope
class OAuthUrlCallbackParser @Inject constructor() {
    fun parseCallback(url: String): Pair<Tokens, String>? {
        val uri = Uri.parse(url)

        val results = uri.fragment
            ?.split("&")
            ?.map { it.split("=") }
            ?.associateBy( { it[0] }, { it[1] } )

        val data = results?.run {
            listOf(ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY, USERNAME_KEY)
                .map { key ->
                    results.getOrDefault(key, null)
                }
        }

        val resultsHasAllData = data?.none { it == null } == true

        return if (resultsHasAllData) {
            val (access, refresh, username) = data!!
            Tokens(access!!, refresh!!) to username!!
        } else {
            null
        }
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val USERNAME_KEY = "account_username"
    }
}
