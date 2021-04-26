package davydov.dmytro.core

import android.os.Bundle
import androidx.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {
    open fun onCreated(savedInstanceState: Bundle?) {}
    open fun onViewCreated() {}
    open fun onViewDestroyed() {}
}