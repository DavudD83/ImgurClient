package davydov.dmytro.root.interceptors

import davydov.dmytro.root.RootScope
import davydov.dmytro.tokens.TokensService
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@RootScope
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