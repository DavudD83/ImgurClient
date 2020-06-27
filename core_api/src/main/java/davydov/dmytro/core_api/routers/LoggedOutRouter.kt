package davydov.dmytro.core_api.routers

import androidx.fragment.app.FragmentTransaction


interface LoggedOutRouter {
    fun moveToLoggedOut(containerId: Int, transaction: FragmentTransaction)
}