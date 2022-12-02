package davydov.dmytro.feature_account.ui

import androidx.lifecycle.viewModelScope
import com.example.network.RetryWhenConnectedUseCase
import davydov.dmytro.core.BaseViewModel
import davydov.dmytro.core_api.StringRepository
import davydov.dmytro.data_user.domain.GetUserNameUseCase
import davydov.dmytro.data_user.domain.LoadUserUseCase
import davydov.dmytro.feature_account.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val loadUserUseCase: LoadUserUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val stringRepository: StringRepository,
    private val retryWhenConnectedUseCase: RetryWhenConnectedUseCase
) : BaseViewModel() {

    init {
        loadUser()
    }

    private val _userState = MutableStateFlow(UserUiState())
    val userState: StateFlow<UserUiState> = _userState.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    private fun loadUser() {
        viewModelScope.launch {
            try {
                val userName = getUserNameUseCase()
                _userState.update { it.copy(name = userName) }
                val user = loadUserUseCase()
                _userState.emit(
                    user.run {
                        UserUiState(
                            name = name,
                            avatarUrl = avatarUrl,
                            coverUrl = coverUrl,
                            reputationName = reputationName
                        )
                    }
                )
            } catch (error: Throwable) {
                _errorMessage.emit(stringRepository.getString(R.string.load_user_error_message))
                retryWhenConnectedUseCase.handleError(error, ::loadUser)
            }
        }
    }
}