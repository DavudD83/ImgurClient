package davydov.dmytro.imgurclient.root.loggedOut

import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.base.BaseFragment

class LoggedOutFragment : BaseFragment<LoggedOutViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_logged_out
    override val vmClass: Class<LoggedOutViewModel>
        get() = LoggedOutViewModel::class.java

    companion object {
        fun newInstance() = LoggedOutFragment()
    }
}
