package davydov.dmytro.data_user.domain

import davydov.dmytro.core_api.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): User = withContext(dispatcher) {
        userRepository.loadUser().also { userRepository.saveUserName(it.name) }
    }
}