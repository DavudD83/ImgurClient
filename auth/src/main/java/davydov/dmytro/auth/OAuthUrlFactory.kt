package davydov.dmytro.auth

import android.net.Uri
import javax.inject.Inject

@LoggedOutScope
class OAuthUrlFactory @Inject constructor() {
    fun createUrl(): String {
        return Uri.parse(AUTH_URL)
            .buildUpon()
            .appendQueryParameter("client_id", BuildConfig.IMGUR_CLIENT_ID)
            .appendQueryParameter("response_type", "token")
            .build()
            .toString()
    }

    companion object {
        private const val AUTH_URL = "https://api.imgur.com/oauth2/authorize"
    }
}
