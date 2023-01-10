package davydov.dmytro.feature_account.posts.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import davydov.dmytro.feature_account.posts.domain.Image
import davydov.dmytro.feature_account.posts.domain.LoadPostsUseCase.Companion.FIRST_PAGE
import davydov.dmytro.feature_account.posts.domain.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PostPagingSource(
    private val accountName: String,
    private val postsApi: PostsApi
) : PagingSource<Int, Post>() {

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val page = params.key ?: FIRST_PAGE
            val accountPosts = postsApi.loadPosts(accountName, page, params.loadSize).data
            val postsWithImages = coroutineScope {
                accountPosts
                    .map {
                        val loadImagesDeferred = async { postsApi.loadPostImages(it.id) }
                        it to loadImagesDeferred
                    }
                    .map { (remotePost, loadImages) ->
                        remotePost to loadImages.await()
                    }
                    .map { (remotePost, remoteImages) ->
                        val postImages = remoteImages.data.map { remote ->
                            Image(
                                id = remote.id,
                                url = remote.url,
                                description = remote.description.orEmpty()
                            )
                        }
                        val status = when (remotePost.privacy) {
                            REMOTE_PRIVATE_STATUS -> Post.PrivacyStatus.HIDDEN
                            REMOTE_PUBLIC_STATUS -> Post.PrivacyStatus.PUBLIC
                            else -> throw IllegalArgumentException("Unknown privacy remote status received")
                        }
                        Post(
                            images = postImages,
                            id = remotePost.id,
                            title = remotePost.title,
                            privacyStatus = status
                        )
                    }
            }
            val nextKey = if (postsWithImages.isEmpty()) {
                null
            } else {
                page + 1
            }
            LoadResult.Page(postsWithImages, prevKey = null, nextKey = nextKey)
        } catch (error: Throwable) {
            LoadResult.Error(error)
        }
    }

    companion object {
        private const val REMOTE_PRIVATE_STATUS = "hidden"
        private const val REMOTE_PUBLIC_STATUS = "public"
    }
}