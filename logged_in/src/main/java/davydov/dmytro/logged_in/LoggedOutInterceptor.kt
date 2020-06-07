package davydov.dmytro.logged_in

import davydov.dmytro.tokens.TokensService
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.lang.IllegalStateException
import javax.inject.Inject

@LoggedInScope
class LoggedOutInterceptor @Inject constructor(private val tokensService: TokensService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return if (response.code == FORBIDDEN) {
            //TODO refresh tokens here
            tokensService.saveTokens(null)
            throw IOException("Logged out")
        } else {
            response
        }
    }

    companion object {
        private const val FORBIDDEN = 403
    }
}