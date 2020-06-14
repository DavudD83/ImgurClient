package davydov.dmytro.logged_in

import android.content.Context
import davydov.dmytro.core.BaseFragment

class LoggedInFragment : BaseFragment<LoggedInViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_logged_in
    override val vmClass: Class<LoggedInViewModel>
        get() = LoggedInViewModel::class.java

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoggedInComponent
            .builder()
            .build()
            .inject(this)
    }

    companion object {
        fun newInstance() = LoggedInFragment()
    }
}
