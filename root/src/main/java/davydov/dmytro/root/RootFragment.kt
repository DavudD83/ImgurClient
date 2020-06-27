package davydov.dmytro.root

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.core_api.routers.LoggedInRouter
import davydov.dmytro.core_api.routers.LoggedOutRouter
import davydov.dmytro.localstorage.AppWithSharedPrefProvider
import davydov.dmytro.tokens.TokensServiceProvider
import davydov.dmytro.tokens.WithTokensProvider
import javax.inject.Inject


class RootFragment : BaseFragment<RootViewModel>(), WithTokensProvider {

    override val layoutId: Int
        get() = R.layout.fragment_root
    override val vmClass: Class<RootViewModel>
        get() = RootViewModel::class.java

    @Inject
    lateinit var loggedOutRouter: LoggedOutRouter
    @Inject
    lateinit var loggedInRouter: LoggedInRouter

    private lateinit var rootComponent: RootComponent

    private var loggedOutAttached: Boolean = false
    private var loggedInAttached: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val application = requireContext().applicationContext
        val facade = (application as AppWithFacade).facade()

        DaggerRootComponent
            .builder()
            .objectMapperProvider(facade)
            .routersProvider(facade)
            .sharedPreferencesProvider((application as AppWithSharedPrefProvider).sharedPrefProvider())
            .build()
            .also { rootComponent = it }
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { restoreState(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.rootState.observe(viewLifecycleOwner, Observer { newState ->
            when (newState) {
                RootViewModel.RootState.LOGGED_OUT -> {
                    if (!loggedOutAttached) {
                        loggedOutRouter.moveToLoggedOut(R.id.container, createTransaction())

                        loggedInAttached = false
                        loggedOutAttached = true
                    }
                }
                RootViewModel.RootState.LOGGED_IN -> {
                    if (!loggedInAttached) {
                        loggedInRouter.moveToLoggedIn(R.id.container, createTransaction())

                        loggedOutAttached = false
                        loggedInAttached = true
                    }
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_LOGGED_IN_ATTACHED, loggedInAttached)
        outState.putBoolean(KEY_LOGGED_OUT_ATTACHED, loggedOutAttached)
    }

    override fun provider(): TokensServiceProvider = rootComponent

    private fun restoreState(state: Bundle) {
        loggedInAttached = state.getBoolean(KEY_LOGGED_IN_ATTACHED)
        loggedOutAttached = state.getBoolean(KEY_LOGGED_OUT_ATTACHED)
    }

    private fun createTransaction(): FragmentTransaction {
        val transaction = childFragmentManager.beginTransaction()

        val currFragment = childFragmentManager.findFragmentById(R.id.container)

        if (currFragment != null) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        }

        return transaction
    }

    companion object {
        fun newInstance() = RootFragment()

        private const val KEY_LOGGED_IN_ATTACHED = "LOGGED_IN_ATTACHED"
        private const val KEY_LOGGED_OUT_ATTACHED = "LOGGED_OUT_ATTACHED"
    }
}
