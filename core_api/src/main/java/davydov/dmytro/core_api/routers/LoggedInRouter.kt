package davydov.dmytro.core_api.routers

import androidx.fragment.app.FragmentManager


interface LoggedInRouter {
    fun moveToLoggedIn(containerId: Int, fragmentManager: FragmentManager)
}