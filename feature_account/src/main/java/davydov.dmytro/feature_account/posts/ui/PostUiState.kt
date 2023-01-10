package davydov.dmytro.feature_account.posts.ui

data class PostUiState(
    val id: String,
    val title: String,
    val multipleImagesInsideVisible: Boolean,
    val imagesCountString: String,
    val coverUrl: String?,
    val privacyString: String
)