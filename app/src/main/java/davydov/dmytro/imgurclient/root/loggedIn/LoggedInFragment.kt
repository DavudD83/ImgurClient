package davydov.dmytro.imgurclient.root.loggedIn

import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.base.BaseFragment

class LoggedInFragment : BaseFragment<LoggedInViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_logged_in
    override val vmClass: Class<LoggedInViewModel>
        get() = LoggedInViewModel::class.java

    companion object {
        fun newInstance() = LoggedInFragment()
    }
}
