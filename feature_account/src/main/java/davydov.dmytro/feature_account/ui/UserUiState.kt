package davydov.dmytro.feature_account.ui

data class UserUiState(
    val name: String = "",
    val avatarUrl: String? = null,
    val coverUrl: String? = null,
    val reputationName: String? = null
)