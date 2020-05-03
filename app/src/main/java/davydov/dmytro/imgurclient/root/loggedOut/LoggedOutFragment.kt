package davydov.dmytro.imgurclient.root.loggedOut

import android.content.Context
import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.base.BaseFragment
import davydov.dmytro.imgurclient.root.RootProvider

class LoggedOutFragment : BaseFragment<LoggedOutViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_logged_out
    override val vmClass: Class<LoggedOutViewModel>
        get() = LoggedOutViewModel::class.java

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoggedOutComponent
            .builder()
            .rootComponent((parentFragment as RootProvider).rootComponent())
            .build()
            .inject(this)
    }

    companion object {
        fun newInstance() = LoggedOutFragment()
    }
}
