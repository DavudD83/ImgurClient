package davydov.dmytro.logged_in.interceptors

import com.example.network.model.ApiError
import com.example.network.model.ErrorResponse
import com.example.network.model.GenericError
import com.fasterxml.jackson.databind.ObjectMapper
import davydov.dmytro.logged_in.LoggedInScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@LoggedInScope
class ApiErrorInterceptor @Inject constructor(private val objectMapper: ObjectMapper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return if (response.isSuccessful) {
            response
        } else {
            try {
                val errorResponse = objectMapper.readValue(response.body!!.byteStream(), ErrorResponse::class.java)
                throw ApiError(errorResponse.data.error, errorResponse.status)
            } catch (ex: Throwable) {
                throw GenericError(ex)
            }
        }
    }
}