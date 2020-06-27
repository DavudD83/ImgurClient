package davydov.dmytro.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import davydov.dmytro.core.BaseFragment
import davydov.dmytro.core.handleEvent
import davydov.dmytro.core_api.AppWithFacade
import davydov.dmytro.tokens.WithTokensProvider
import kotlinx.android.synthetic.main.fragment_logged_out.*
import kotlinx.android.synthetic.main.fragment_logged_out.view.*

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
            .providersFacade((requireContext().applicationContext as AppWithFacade).facade())
            .tokensServiceProvider((parentFragment as WithTokensProvider).provider())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scaleX =
            ObjectAnimator.ofFloat(view.logIn, View.SCALE_X, 0.3f, 1f).apply { duration = 300L }
        val scaleY =
            ObjectAnimator.ofFloat(view.logIn, View.SCALE_Y, 0.3f, 1f).apply { duration = 300L }

        AnimatorSet().run {
            playTogether(scaleX, scaleY)
            start()
        }

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
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView, url: String) {
                                window!!
                                    .decorView
                                    .animate()
                                    .alpha(1f)
                                    .apply {
                                        duration = WEB_VIEW_ANIM_TIME
                                    }

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

        private const val WEB_VIEW_ANIM_TIME = 200L
    }
}
