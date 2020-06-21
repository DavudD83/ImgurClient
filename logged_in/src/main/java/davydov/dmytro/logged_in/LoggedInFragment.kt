package davydov.dmytro.logged_in

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.network.GalleriesApiProvider
import com.example.network.WithGalleriesApiProvider
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.core_api.routers.ViralGalleriesRouter
import davydov.dmytro.tokens.WithTokensProvider
import javax.inject.Inject

class LoggedInFragment : BaseFragment<LoggedInViewModel>(), WithGalleriesApiProvider {

    override val layoutId: Int
        get() = R.layout.fragment_logged_in
    override val vmClass: Class<LoggedInViewModel>
        get() = LoggedInViewModel::class.java

    @Inject
    lateinit var viralGalleriesRouter: ViralGalleriesRouter

    private lateinit var component: LoggedInComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoggedInComponent
            .builder()
            .providersFacade((requireContext().applicationContext as AppWithFacade).facade())
            .tokensServiceProvider((parentFragment as WithTokensProvider).provider())
            .build()
            .also { component = it }
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState ?: viralGalleriesRouter.moveToViralGalleries(R.id.container, childFragmentManager)
    }

    override fun provider(): GalleriesApiProvider = component

    companion object {
        fun newInstance() = LoggedInFragment()
    }
}
