package davydov.dmytro.core

import androidx.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {
    open fun onViewCreated() {}
    open fun onViewDestroyed() {}
}