package davydov.dmytro.feature_account.posts.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import davydov.dmytro.feature_account.posts.data.PostPagingSourceFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadPostsUseCase @Inject constructor(private val pagingSourceFactory: PostPagingSourceFactory) {

    private val pagingConfig =
        PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = PREFETCH_DISTANCE
        )

    operator fun invoke(accountName: String): Flow<PagingData<Post>> {
        pagingSourceFactory.accountName = accountName
        return Pager(pagingConfig, initialKey = FIRST_PAGE, pagingSourceFactory::create).flow
    }

    companion object {
        const val FIRST_PAGE = 0
        private const val PAGE_SIZE = 10
        private const val PREFETCH_DISTANCE = PAGE_SIZE / 2
    }
}