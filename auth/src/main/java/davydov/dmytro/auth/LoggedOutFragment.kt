package davydov.dmytro.auth

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.network.WithRetrofitProvider
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core.findParentProvider
import davydov.dmytro.core.handleEvent
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.localstorage.AppWithSharedPrefProvider
import davydov.dmytro.tokens.WithTokensProvider
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
            .providersFacade(findParentProvider<AppWithFacade>().facade())
            .tokensServiceProvider(findParentProvider<WithTokensProvider>().provider())
            .retrofitProvider(findParentProvider<WithRetrofitProvider>().retrofitProvider())
            .sharedPreferencesProvider(findParentProvider<AppWithSharedPrefProvider>().sharedPrefProvider())
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
            Dialog(requireContext(), R.style.OAuthDialogTheme)
                .apply {
                    setContentView(R.layout.layout_oauth_dialog)
                    show()

                    window!!.decorView.alpha = 0f

                    setOnDismissListener { authDialog = null }

                    findViewById<WebView>(R.id.webView)?.run {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webViewClient = object : WebViewClient() {
                            override fun onReceivedError(
                                view: WebView?,
                                errorCode: Int,
                                description: String?,
                                failingUrl: String?
                            ) {
                                viewModel.onWebViewError()
                            }

                            override fun onPageFinished(view: WebView, url: String) {
                                window!!
                                    .decorView
                                    .animate()
                                    .alpha(1f)
                                    .apply {
                                        duration = WEB_VIEW_ANIM_TIME
                                    }

                                //account_username
                                viewModel.onWebViewPageLoaded(url)
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest
                            ): Boolean {
                                return viewModel.shouldOverrideUrlLoading(request.url.toString())
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

        private const val WEB_VIEW_ANIM_TIME = 200L
    }
}
