package davydov.dmytro.core_api

import androidx.annotation.StringRes

interface StringRepository {
    fun getString(@StringRes idRes: Int): String
}