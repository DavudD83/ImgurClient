package davydov.dmytro.logged_in.interceptors

import davydov.dmytro.logged_in.LoggedInScope
import davydov.dmytro.tokens.TokensService
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@LoggedInScope
class LoggedOutInterceptor @Inject constructor(private val tokensService: TokensService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return if (response.code == FORBIDDEN) {
            //TODO refresh tokens here
            tokensService.saveTokens(null)
            response
        } else {
            response
        }
    }

    companion object {
        private const val FORBIDDEN = 403
    }
}