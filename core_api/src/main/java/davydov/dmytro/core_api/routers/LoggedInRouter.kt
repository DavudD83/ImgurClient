package davydov.dmytro.core_api.routers

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


interface LoggedInRouter {
    fun moveToLoggedIn(containerId: Int, fragmentTransaction: FragmentTransaction)
}