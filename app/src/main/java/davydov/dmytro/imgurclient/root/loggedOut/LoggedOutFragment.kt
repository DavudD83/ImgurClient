package davydov.dmytro.imgurclient.root.loggedOut

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import davydov.dmytro.imgurclient.R
import davydov.dmytro.imgurclient.base.BaseFragment
import davydov.dmytro.imgurclient.base.handleEvent
import davydov.dmytro.imgurclient.root.RootProvider
import kotlinx.android.synthetic.main.fragment_logged_out.*

class LoggedOutFragment : BaseFragment<LoggedOutViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_logged_out
    override val vmClass: Class<LoggedOutViewModel>
        get() = LoggedOutViewModel::class.java

    private var authDialog: Dialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerLoggedOutComponent
            .builder()
            .rootComponent((parentFragment as RootProvider).rootComponent())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logIn.setOnClickListener { viewModel.onLogInClick() }

        viewModel.showAuthPopUp.observe(viewLifecycleOwner, Observer { popupState ->
            when (popupState) {
                is LoggedOutViewModel.AuthPopupState.Shown -> loadUrlInDialog(popupState.url)
                LoggedOutViewModel.AuthPopupState.Hidden -> authDialog?.dismiss()
            }
        })

        viewModel.showAuthError.handleEvent(this) { message ->
            AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun loadUrlInDialog(url: String) {
        if (authDialog == null) {
            Dialog(requireContext())
                .apply {
                    setContentView(R.layout.layout_oauth_dialog)
                    show()

                    setOnDismissListener { authDialog = null }

                    findViewById<WebView>(R.id.webView)?.run {
                        webViewClient = object: WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String) {
                                viewModel.onWebViewPageLoaded(url)
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest
                            ): Boolean {
                                return viewModel.shouldOverrideUrlLoading(request.url.toString())
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                viewModel.onWebViewError()
                            }
                        }

                        loadUrl(url)
                    }
                }
                .also { authDialog = it }
        }
    }

    companion object {
        fun newInstance() = LoggedOutFragment()
    }
}
