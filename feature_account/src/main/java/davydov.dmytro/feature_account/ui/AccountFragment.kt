package davydov.dmytro.feature_account.ui


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core_ui.loadImage
import com.example.core_ui.showSnackbar
import com.example.network.WithRetrofitProvider
import com.example.network.WithServiceProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core.findParentProvider
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.feature_account.R
import davydov.dmytro.feature_account.databinding.FragmentAccountBinding
import davydov.dmytro.feature_account.di.DaggerAccountComponent
import davydov.dmytro.localstorage.AppWithSharedPrefProvider
import kotlinx.coroutines.launch

class AccountFragment : BaseFragment<AccountViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_account
    override val vmClass: Class<AccountViewModel>
        get() = AccountViewModel::class.java

    override fun onAttach(context: Context) {
        DaggerAccountComponent.factory()
            .build(
                findParentProvider<AppWithFacade>().facade(),
                findParentProvider<WithRetrofitProvider>().retrofitProvider(),
                findParentProvider<AppWithSharedPrefProvider>().sharedPrefProvider(),
                findParentProvider<WithServiceProvider>().serviceProvider()
            )
            .inject(this)
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
        avatar.loadImage(url = state.avatarUrl, applyCircleCrop = true, skipCache = true)
        cover.loadImage(url = state.coverUrl, skipCache = true)
        username.text = state.name
        reputationName.text = state.reputationName
    }
}