package davydov.dmytro.imgurclient.root

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.application.ImgurClientApplication
import davydov.dmytro.imgurclient.base.BaseFragment
import davydov.dmytro.imgurclient.root.loggedIn.LoggedInFragment
import davydov.dmytro.imgurclient.root.loggedOut.LoggedOutFragment


class RootFragment : BaseFragment<RootViewModel>(), RootProvider {

    override val layoutId: Int
        get() = R.layout.fragment_root
    override val vmClass: Class<RootViewModel>
        get() = RootViewModel::class.java

    lateinit var rootComponent: RootComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerRootComponent
            .builder()
            .applicationComponent(ImgurClientApplication.appComponent)
            .build()
            .also { rootComponent = it }
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.rootState.observe(viewLifecycleOwner, Observer { newState ->
            val nextFragment = createFragmentBy(newState)

            if (currFragment?.javaClass != nextFragment.javaClass) {
                replaceFragment(nextFragment)
            }
        })
    }

    override fun rootComponent(): RootComponent = rootComponent

    private fun createFragmentBy(state: RootViewModel.RootState): Fragment {
        return when (state) {
            RootViewModel.RootState.LOGGED_OUT -> LoggedOutFragment.newInstance()
            RootViewModel.RootState.LOGGED_IN -> LoggedInFragment.newInstance()
        }
    }

    companion object {
        fun newInstance() = RootFragment()
    }
}
