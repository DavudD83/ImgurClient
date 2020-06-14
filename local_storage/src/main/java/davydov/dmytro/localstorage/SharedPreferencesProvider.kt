package davydov.dmytro.localstorage

import android.content.SharedPreferences


interface SharedPreferencesProvider {
    fun sharedPreferences(): SharedPreferences
}