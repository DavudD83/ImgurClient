package davydov.dmytro.logged_in

import androidx.fragment.app.FragmentManager
import davydov.dmytro.core_api.routers.LoggedInRouter
import javax.inject.Inject


class LoggedInRouterImpl @Inject constructor() : LoggedInRouter {
    override fun moveToLoggedIn(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .replace(containerId, LoggedInFragment.newInstance())
            .commit()
    }
}