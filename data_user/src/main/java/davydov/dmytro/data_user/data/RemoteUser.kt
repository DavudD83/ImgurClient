package davydov.dmytro.data_user.data

import davydov.dmytro.data_user.domain.User

data class RemoteUser(
    val url: String?,
    val avatar: String?,
    val cover: String?,
    val reputationName: String?
) {
    fun toDomain(): User = User(
        name = url.orEmpty(),
        avatarUrl = avatar.orEmpty(),
        coverUrl = cover.orEmpty(),
        reputationName = reputationName.orEmpty()
    )
}