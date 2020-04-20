package davydov.dmytro.imgurclient.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import davydov.dmytro.imgurclient.R
import javax.inject.Inject


abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected abstract val layoutId: Int
    protected abstract val vmClass: Class<VM>

    protected lateinit var viewModel: VM

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<VM>

    protected val currFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.container)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[vmClass]
        viewModel.onViewCreated()
    }

    override fun onDestroyView() {
        viewModel.onViewDestroyed()
        super.onDestroyView()
    }

    protected fun replaceFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()

        if (currFragment != null) {
            transaction.addToBackStack(null)
        }

        transaction
            .replace(R.id.container, fragment)
            .commit()
    }
}