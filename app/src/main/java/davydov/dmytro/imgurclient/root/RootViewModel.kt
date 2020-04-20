package davydov.dmytro.imgurclient.root

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import davydov.dmytro.imgurclient.base.BaseViewModel
import javax.inject.Inject

@RootScope
class RootViewModel @Inject constructor(private val userExistenceInteractor: UserExistenceInteractor) : BaseViewModel(),
    UserExistenceInteractor.Listener {

    private val _rootState = MutableLiveData<RootState>()
    val rootState: LiveData<RootState> = _rootState

    override fun onViewCreated() {
        userExistenceInteractor.start()
    }

    override fun navigateToLoggedIn() {
        _rootState.value = RootState.LOGGED_IN
    }

    override fun navigateToLoggedOut() {
        _rootState.value = RootState.LOGGED_OUT
    }

    enum class RootState {
        LOGGED_OUT, LOGGED_IN
    }
}
