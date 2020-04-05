package davydov.dmytro.imgurclient.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import davydov.dmytro.imgurclient.root.RootViewModel
import davydov.dmytro.imgurclient.root.TokensService
import davydov.dmytro.imgurclient.root.UserExistenceInteractor
import java.lang.IllegalArgumentException

//TODO Setup DI here later
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RootViewModel::class.java -> {
                val tokenService = TokensService(
                    context.getSharedPreferences("imgurPreferences", Context.MODE_PRIVATE),
                    jacksonObjectMapper()
                )

                val userExistenceInteractor = UserExistenceInteractor(tokenService)
                val viewModel = RootViewModel(userExistenceInteractor)
                userExistenceInteractor.listener = viewModel

                viewModel
            }
            else -> {
              throw IllegalArgumentException("Unknown model class")
            }
        } as T
    }
}