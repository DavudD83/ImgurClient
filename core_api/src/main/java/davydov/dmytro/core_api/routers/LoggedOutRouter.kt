package davydov.dmytro.core_api.routers

import androidx.fragment.app.FragmentManager


interface LoggedOutRouter {
    fun moveToLoggedOut(containerId: Int, fragmentManager: FragmentManager)
}