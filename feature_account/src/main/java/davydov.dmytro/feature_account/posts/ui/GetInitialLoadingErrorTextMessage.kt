package davydov.dmytro.feature_account.posts.ui

import android.text.style.UnderlineSpan
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import davydov.dmytro.core_api.StringRepository
import davydov.dmytro.feature_account.R
import javax.inject.Inject

class GetInitialLoadingErrorTextMessage @Inject constructor(
    private val stringRepository: StringRepository
) {
    operator fun invoke(): CharSequence {
        val include =
            stringRepository.getString(R.string.initial_posts_loading_error_message_underscored_include)
        val message =
            stringRepository.getString(R.string.initial_posts_loading_error_message, include)
        val startIndex = message.indexOf(include)
        val endIndex = startIndex + include.length
        return buildSpannedString {
            append(message)
            set(startIndex, endIndex, UnderlineSpan())
        }
    }
}
