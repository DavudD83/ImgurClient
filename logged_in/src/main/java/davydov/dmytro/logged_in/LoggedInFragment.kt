package davydov.dmytro.logged_in

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.forEach
import androidx.core.view.updateLayoutParams
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.network.*
import com.google.android.material.navigation.NavigationBarView
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core.findParentProvider
import davydov.dmytro.core.setupContainerBackNavigation
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.core_api.routers.ViralGalleriesRouter
import davydov.dmytro.logged_in.databinding.FragmentLoggedInBinding
import java.lang.ref.WeakReference
import javax.inject.Inject

class LoggedInFragment : BaseFragment<LoggedInViewModel>(), WithGalleriesApiProvider,
    WithServiceProvider {

    override val layoutId: Int
        get() = R.layout.fragment_logged_in
    override val vmClass: Class<LoggedInViewModel>
        get() = LoggedInViewModel::class.java

    @Inject
    lateinit var viralGalleriesRouter: ViralGalleriesRouter

    @Inject
    lateinit var connectionStateService: ConnectionStateService

    private lateinit var component: LoggedInComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoggedInComponent
            .builder()
            .providersFacade((requireContext().applicationContext as AppWithFacade).facade())
            .retrofitProvider(findParentProvider<WithRetrofitProvider>().retrofitProvider())
            .build()
            .also { component = it }
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoggedInBinding.bind(view)
        val navController = binding.container.getFragment<NavHostFragment>().navController
        setupContainerBackNavigation(navController)
        setupWithNavController(binding.bottomNavigation, navController) {
            val marginBottom = if (it.itemId == R.id.home) {
                0
            } else {
                binding.bottomNavigation.height
            }
            binding.container.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = marginBottom
            }
        }
    }

    override fun onDestroy() {
        connectionStateService.close()
        super.onDestroy()
    }

    override fun provider(): GalleriesApiProvider = component
    override fun serviceProvider(): ConnectivityServiceProvider = component

    private fun setupWithNavController(
        navigationBarView: NavigationBarView,
        navController: NavController,
        onItemSelectedAction: (MenuItem) -> Unit
    ) {
        navigationBarView.setOnItemSelectedListener { item ->
            onItemSelectedAction(item)
            NavigationUI.onNavDestinationSelected(
                item,
                navController
            )
        }
        val weakReference = WeakReference(navigationBarView)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    view.menu.forEach { item ->
                        if (destination.hierarchy.any { it.id == item.itemId }) {
                            item.isChecked = true
                        }
                    }
                }
            })
    }

    companion object {
        fun newInstance() = LoggedInFragment()
    }
}
