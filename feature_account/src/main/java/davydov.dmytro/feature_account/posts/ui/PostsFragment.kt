package davydov.dmytro.feature_account.posts.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.feature_account.R
import davydov.dmytro.feature_account.databinding.FragmentPostsBinding
import davydov.dmytro.feature_account.ui.AccountFragment
import kotlinx.coroutines.launch

class PostsFragment : BaseFragment<PostsViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_posts
    override val vmClass: Class<PostsViewModel>
        get() = PostsViewModel::class.java

    override fun onAttach(context: Context) {
        (parentFragment as AccountFragment).component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPostsBinding.bind(view)
        val adapter = PostsAdapter()
        val footerAdapter = FooterLoadingAdapter(adapter::retry)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.onPullToRefresh()
            adapter.refresh()
        }
        binding.posts.adapter = adapter.withLoadStateFooter(footerAdapter)
        binding.posts.layoutManager =
            GridLayoutManager(requireContext(), POSTS_COUNT_IN_ROW).apply {
                spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val lastItem = position == binding.posts.adapter!!.itemCount - 1
                        val loadingOrErrorState =
                            footerAdapter.loadState is LoadState.Error ||
                                    footerAdapter.loadState is LoadState.Loading
                        val loadingItem = lastItem && loadingOrErrorState
                        return if (loadingItem) {
                            POSTS_COUNT_IN_ROW
                        } else {
                            1
                        }
                    }
                }
            }
        binding.posts.addItemDecoration(PostsItemDecoration(requireContext()))
        binding.layoutError.root.setOnClickListener {
            adapter.retry()
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { state: CombinedLoadStates ->
                    viewModel.onPostsLoadStateChanged(state, adapter.itemCount)
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.posts.collect {
                    adapter.submitData(it)
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.generalUiState.collect {
                    binding.initialLoading.isVisible = it.initialLoadingVisible
                    binding.posts.isVisible = it.postsListVisible
                    binding.layoutError.root.isVisible = it.initialErrorVisible
                    binding.layoutError.errorText.text = it.initialErrorMessage
                    binding.layoutEmpty.root.isVisible = it.emptyPostsVisible
                    binding.layoutEmpty.emptyText.text = it.emptyPostsMessage
                    binding.swipeRefresh.isRefreshing = it.pullToRefreshLoadingVisible
                }
            }
        }
    }

    companion object {
        const val POSTS_COUNT_IN_ROW = 2
    }
}