package davydov.dmytro.logged_in

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.network.GalleriesApiProvider
import com.example.network.WithGalleriesApiProvider
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.core_api.GoBackListener
import davydov.dmytro.core_api.OnGalleryChosen
import davydov.dmytro.core_api.routers.GalleryDetailsRouter
import davydov.dmytro.core_api.routers.ViralGalleriesRouter
import davydov.dmytro.tokens.WithTokensProvider
import javax.inject.Inject

class LoggedInFragment : BaseFragment<LoggedInViewModel>(), WithGalleriesApiProvider,
    GoBackListener, OnGalleryChosen {

    override val layoutId: Int
        get() = R.layout.fragment_logged_in
    override val vmClass: Class<LoggedInViewModel>
        get() = LoggedInViewModel::class.java

    @Inject
    lateinit var viralGalleriesRouter: ViralGalleriesRouter

    @Inject
    lateinit var galleryDetailsRouter: GalleryDetailsRouter

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

    override fun goBack() {
        childFragmentManager.popBackStack()
    }

    override fun provider(): GalleriesApiProvider = component

    override fun onGalleryChosen(id: String, name: String) {
        val transaction = childFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)

        galleryDetailsRouter.navigateToGalleryDetails(R.id.container, transaction, id, name)
    }

    companion object {
        fun newInstance() = LoggedInFragment()
    }
}
