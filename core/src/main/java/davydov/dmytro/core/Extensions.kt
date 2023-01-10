package davydov.dmytro.core

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

inline val Any?.exhaustive: Any?
    get() = this

inline fun <reified T> Fragment.findParentProvider(): T {
    val application = requireActivity().application
    val isApplicationProvider = application is T
    if (isApplicationProvider) {
        return application as T
    } else {
        var parent = parentFragment
        while (parent != null) {
            if (parent is T) {
                return parent
            } else {
                parent = parent.parentFragment
            }
        }
        throw IllegalArgumentException("Could not find provider ${T::class} in the whole hierarchy")
    }
}

fun Fragment.setupContainerBackNavigation(navController: NavController? = null) {
    val controller = navController ?: findNavController()
    val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        controller.navigateUp()
    }
    controller.addOnDestinationChangedListener { _, _, _ ->
        onBackPressedCallback.isEnabled = controller.previousBackStackEntry != null
    }
}