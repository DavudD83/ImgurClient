package davydov.dmytro.auth

import androidx.fragment.app.FragmentManager
import davydov.dmytro.core_api.routers.LoggedOutRouter
import javax.inject.Inject


class LoggedOutRouterImpl @Inject constructor() : LoggedOutRouter {
    override fun moveToLoggedOut(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, LoggedOutFragment.newInstance())
            .commit()
    }
}