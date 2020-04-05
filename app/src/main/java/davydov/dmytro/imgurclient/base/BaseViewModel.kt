package davydov.dmytro.imgurclient.base

import androidx.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {
    fun onViewCreated() {}
    fun onViewDestroyed() {}
}