package davydov.dmytro.data_user.domain

interface UserRepository {
    suspend fun loadUser(): User
    fun saveUserName(name: String)
    fun getUserName(): String
}