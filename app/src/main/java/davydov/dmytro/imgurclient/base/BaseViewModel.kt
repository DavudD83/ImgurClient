package davydov.dmytro.imgurclient.base

import androidx.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {
    open fun onViewCreated() {}
    open fun onViewDestroyed() {}
}