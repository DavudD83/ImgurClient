package davydov.dmytro.feature_account.posts.data

import javax.inject.Inject

class PostPagingSourceFactory @Inject constructor(private val postsApi: PostsApi) {

    lateinit var accountName: String

    fun create(): PostPagingSource {
        return PostPagingSource(accountName, postsApi)
    }
}