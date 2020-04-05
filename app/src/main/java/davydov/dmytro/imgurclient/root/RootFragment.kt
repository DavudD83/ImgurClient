package davydov.dmytro.imgurclient.root

import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.base.BaseFragment


class RootFragment : BaseFragment<RootViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_root
    override val vmClass: Class<RootViewModel>
        get() = RootViewModel::class.java

    companion object {
        fun newInstance() = RootFragment()
    }
}
