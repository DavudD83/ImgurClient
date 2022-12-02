package davydov.dmytro.data_user.domain

import davydov.dmytro.core_api.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): String = withContext(dispatcher) {
        userRepository.getUserName()
    }
}