package davydov.dmytro.feature_account.posts.data

import com.example.network.model.ApiResponse
import com.example.network.model.RemoteImage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApi {

    @GET("account/{$ACCOUNT_NAME}/albums/{$PAGE_PATH}")
    suspend fun loadPosts(
        @Path(ACCOUNT_NAME) accountName: String,
        @Path(PAGE_PATH) page: Int,
        @Query(PER_PAGE) pageSize: Int
    ): ApiResponse<List<RemotePost>>

    @GET("album/{$POST_ID}/images")
    suspend fun loadPostImages(@Path(POST_ID) postId: String): ApiResponse<List<RemoteImage>>

    companion object {
        private const val ACCOUNT_NAME = "account"
        private const val PAGE_PATH = "page"
        private const val POST_ID = "postId"
        private const val PER_PAGE = "perPage"
    }
}