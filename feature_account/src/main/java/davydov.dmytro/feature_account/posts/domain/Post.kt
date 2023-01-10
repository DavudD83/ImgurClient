package davydov.dmytro.feature_account.posts.domain


data class Post(
    val images: List<Image>,
    val id: String,
    val title: String,
    val privacyStatus: PrivacyStatus
) {

    enum class PrivacyStatus {
        HIDDEN, PUBLIC
    }
}