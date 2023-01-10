package davydov.dmytro.feature_account.posts.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import davydov.dmytro.core.BaseViewModel
import davydov.dmytro.core_api.StringRepository
import davydov.dmytro.data_user.domain.GetUserNameUseCase
import davydov.dmytro.feature_account.R
import davydov.dmytro.feature_account.posts.domain.LoadPostsUseCase
import davydov.dmytro.feature_account.posts.domain.Post
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PostsViewModel @Inject constructor(
    private val loadPostsUseCase: LoadPostsUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val stringRepository: StringRepository,
    private val getInitialLoadingErrorTextMessage: GetInitialLoadingErrorTextMessage
) : BaseViewModel() {

    private val _posts = MutableStateFlow<PagingData<Post>>(PagingData.empty())
    val posts: Flow<PagingData<PostUiState>> = _posts.map {
        it.map { post -> mapPostToUiState(post) }
    }

    private val _generalUiState = MutableStateFlow(GeneralScreenUiState())
    val generalUiState: StateFlow<GeneralScreenUiState> = _generalUiState

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            val userName = getUserNameUseCase()
            loadPostsUseCase(userName)
                .cachedIn(viewModelScope)
                .catch {
                    Timber.e(
                        it,
                        "Error during listening to posts paging data update. Can not recover"
                    )
                }
                .collect {
                    _posts.emit(it)
                }
        }
    }

    fun onPostsLoadStateChanged(newState: CombinedLoadStates, itemsCount: Int) {
        viewModelScope.launch {
            _generalUiState.update {
                val errorText = if (newState.refresh is LoadState.Error) {
                    getInitialLoadingErrorTextMessage()
                } else {
                    null
                }
                val noItems = itemsCount == 0
                val emptyText = if (newState.append.endOfPaginationReached && noItems) {
                    stringRepository.getString(R.string.posts_empty_message)
                } else {
                    null
                }
                val pullToRefreshVisible =
                    newState.refresh is LoadState.Loading && it.pullToRefreshLoadingVisible
                val postsListVisible =
                    (newState.refresh is LoadState.NotLoading && emptyText == null) || pullToRefreshVisible
                it.copy(
                    initialErrorMessage = errorText,
                    initialLoadingVisible = newState.refresh is LoadState.Loading && !pullToRefreshVisible,
                    postsListVisible = postsListVisible,
                    emptyPostsMessage = emptyText,
                    pullToRefreshLoadingVisible = pullToRefreshVisible
                )
            }
        }
    }

    fun onPullToRefresh() {
        viewModelScope.launch {
            _generalUiState.update { it.copy(pullToRefreshLoadingVisible = true) }
        }
    }

    private fun mapPostToUiState(post: Post): PostUiState {
        return post.run {
            val privacyStatusStringRes = when (privacyStatus) {
                Post.PrivacyStatus.HIDDEN -> R.string.hidden_status
                Post.PrivacyStatus.PUBLIC -> R.string.public_status
            }
            PostUiState(
                id = id,
                title = post.title,
                multipleImagesInsideVisible = post.images.count() > 1,
                imagesCountString = post.images.count().toString(),
                coverUrl = post.images.firstOrNull()?.url,
                privacyString = stringRepository.getString(privacyStatusStringRes)
            )
        }
    }
}