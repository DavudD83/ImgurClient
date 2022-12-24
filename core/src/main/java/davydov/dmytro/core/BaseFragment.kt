package davydov.dmytro.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject


abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected abstract val layoutId: Int
    protected abstract val vmClass: Class<VM>

    lateinit var viewModel: VM

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[vmClass]
        viewModel.onCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated()
    }

    override fun onDestroyView() {
        viewModel.onViewDestroyed()
        super.onDestroyView()
    }
}