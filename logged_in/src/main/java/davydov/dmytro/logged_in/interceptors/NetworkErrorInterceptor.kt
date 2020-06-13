package davydov.dmytro.logged_in.interceptors

import com.example.network.model.NetworkError
import davydov.dmytro.logged_in.LoggedInScope
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@LoggedInScope
class NetworkErrorInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (ex: IOException) {
            throw NetworkError()
        }
    }
}