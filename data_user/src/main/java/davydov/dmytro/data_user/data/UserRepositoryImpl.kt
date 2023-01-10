package davydov.dmytro.data_user.data

import android.content.SharedPreferences
import davydov.dmytro.data_user.domain.User
import davydov.dmytro.data_user.domain.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    companion object {
        private const val KEY_USER_NAME = "KEY_USER_NAME"
    }

    override suspend fun loadUser(): User {
        val response = userApi.loadUser()
        return response
            .data
            .toDomain()
    }

    override fun saveUserName(name: String) {
        sharedPreferences.edit()
            .putString(KEY_USER_NAME, name)
            .apply()
    }

    override fun getUserName(): String = sharedPreferences.getString(KEY_USER_NAME, "").orEmpty()
}