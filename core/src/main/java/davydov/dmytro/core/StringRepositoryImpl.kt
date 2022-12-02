package davydov.dmytro.core

import android.content.res.Resources
import davydov.dmytro.core_api.StringRepository
import javax.inject.Inject

class StringRepositoryImpl @Inject constructor(private val resources: Resources) :
    StringRepository {

    override fun getString(idRes: Int): String {
        return resources.getString(idRes)
    }
}