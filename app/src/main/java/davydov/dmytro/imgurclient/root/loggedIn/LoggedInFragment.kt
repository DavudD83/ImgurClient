package davydov.dmytro.imgurclient.root.loggedIn

import android.content.Context
import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.base.BaseFragment
import davydov.dmytro.imgurclient.root.RootProvider

class LoggedInFragment : BaseFragment<LoggedInViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_logged_in
    override val vmClass: Class<LoggedInViewModel>
        get() = LoggedInViewModel::class.java

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoggedInComponent
            .builder()
            .rootComponent((parentFragment as RootProvider).rootComponent())
            .build()
            .inject(this)
    }

    companion object {
        fun newInstance() = LoggedInFragment()
    }
}
