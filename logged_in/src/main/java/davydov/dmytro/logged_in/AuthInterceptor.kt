package davydov.dmytro.logged_in

import davydov.dmytro.tokens.TokensService
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@LoggedInScope
class AuthInterceptor @Inject constructor(private val tokensService: TokensService): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newUrlWithClientId = request
            .url
            .newBuilder()
            .addQueryParameter("client_id", BuildConfig.IMGUR_CLIENT_ID)
            .build()

        val bearer = "Bearer ${tokensService.peekTokens?.accessToken}"

        val newRequest = request
            .newBuilder()
            .url(newUrlWithClientId)
            .addHeader(AUTHORIZATION_HEADER, bearer)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }
}