package davydov.dmytro.imgurclient.root.loggedOut

import android.net.Uri
import davydov.dmytro.imgurclient.root.tokensService.Tokens
import javax.inject.Inject

@LoggedOutScope
class OAuthUrlCallbackParser @Inject constructor() {
    fun parseCallback(url: String): Tokens? {
        val uri = Uri.parse(url)

        val results = uri.fragment
            ?.split("&")
            ?.associateBy({ it.split("=")[0] }, { it.split("=")[1] })

        return if (results?.contains(ACCESS_TOKEN_KEY) == true && results.contains(
                REFRESH_TOKEN_KEY
            )
        ) {
            Tokens(
                results[ACCESS_TOKEN_KEY]!!,
                results[REFRESH_TOKEN_KEY]!!
            )
        } else {
            null
        }
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}
