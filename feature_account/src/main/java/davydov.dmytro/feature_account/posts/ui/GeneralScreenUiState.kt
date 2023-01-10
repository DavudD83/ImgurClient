package davydov.dmytro.feature_account.posts.ui

data class GeneralScreenUiState(
    val initialErrorMessage: CharSequence? = null,
    val emptyPostsMessage: String? = null,
    val initialLoadingVisible: Boolean = false,
    val postsListVisible: Boolean = false,
    val pullToRefreshLoadingVisible: Boolean = false
) {
    val initialErrorVisible
        get() = initialErrorMessage != null

    val emptyPostsVisible: Boolean
        get() = emptyPostsMessage != null
}