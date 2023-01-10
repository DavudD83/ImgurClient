package davydov.dmytro.data_user.data

import com.example.network.model.ApiResponse
import retrofit2.http.GET

interface UserApi {
    @GET("account/me")
    suspend fun loadUser(): UserResponse
}

data class UserResponse(val data: RemoteUser)