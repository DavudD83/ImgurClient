package davydov.dmytro.feature_account.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core_ui.loadImage
import com.example.core_ui.showSnackbar
import com.example.network.WithRetrofitProvider
import com.example.network.WithServiceProvider
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core.findParentProvider
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.feature_account.R
import davydov.dmytro.feature_account.databinding.FragmentAccountBinding
import davydov.dmytro.feature_account.di.AccountComponent
import davydov.dmytro.feature_account.di.DaggerAccountComponent
import davydov.dmytro.feature_account.posts.ui.PostsFragment
import davydov.dmytro.localstorage.AppWithSharedPrefProvider
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class AccountFragment : BaseFragment<AccountViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_account
    override val vmClass: Class<AccountViewModel>
        get() = AccountViewModel::class.java

    lateinit var component: AccountComponent

    override fun onAttach(context: Context) {
        DaggerAccountComponent.factory()
            .build(
                findParentProvider<AppWithFacade>().facade(),
                findParentProvider<WithRetrofitProvider>().retrofitProvider(),
                findParentProvider<AppWithSharedPrefProvider>().sharedPrefProvider(),
                findParentProvider<WithServiceProvider>().serviceProvider()
            )
            .also { component = it }
            .inject(this)
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is PostsFragment -> {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            fragment.viewModel
                                .generalUiState
                                .distinctUntilChangedBy { it.pullToRefreshLoadingVisible }
                                .filter { it.pullToRefreshLoadingVisible }
                                .collect { viewModel.onPullToRefresh() }
                        }
                    }
                }
            }
        }
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAccountBinding.bind(view)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        binding.postsContainer.doOnLayout {
            val heightWithoutAppBar = it.measuredHeight - binding.appBar.height
            val listener = OnOffsetChangedListener { appBarLayout, verticalOffset ->
                binding.postsContainer.updateLayoutParams<LayoutParams> {
                    height = heightWithoutAppBar - verticalOffset
                }
            }
            binding.appBar.addOnOffsetChangedListener(listener)
            binding.appBar.requestLayout()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect {
                    binding.bindUserState(it)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect {
                    binding.coordinator.showSnackbar(it, Snackbar.LENGTH_SHORT)
                }
            }
        }
    }

    private fun FragmentAccountBinding.bindUserState(state: UserUiState) {
        avatar.loadImage(
            url = state.avatarUrl,
            applyCircleCrop = true,
            skipCache = true
        )
        cover.loadImage(
            url = state.coverUrl,
            skipCache = true
        )
        username.text = state.name
        reputationName.text = state.reputationName
    }
}