package davydov.dmytro.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


data class Event<T>(val data: T) {
    private var hasBeenHandled = false

    fun handleEvent(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true

            data
        }
    }
}

fun <T> LiveData<Event<T>>.handleEvent(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, Observer { event ->
        event.handleEvent()?.let { observer(it) }
    })
}

fun MutableLiveData<Event<Unit>>.sendEvent() {
    value = Event(Unit)
}

fun <T> MutableLiveData<Event<T>>.sendEvent(data: T) {
    value = Event(data)
}