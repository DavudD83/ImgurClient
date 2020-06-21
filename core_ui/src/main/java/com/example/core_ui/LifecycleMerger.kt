package com.example.core_ui

import androidx.lifecycle.*

class LifecycleMerger private constructor(private vararg val lifecycles: Lifecycle): LifecycleOwner, LifecycleObserver {

    private lateinit var lifecycle: LifecycleRegistry

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
        val currState = lifecycles.minBy { it.currentState.ordinal }?.currentState
        currState?.let { lifecycle.currentState = it }
    }

    companion object {
        fun create(vararg lifecycle: Lifecycle): LifecycleMerger {
            val merger = LifecycleMerger(*lifecycle)
            merger.lifecycle = LifecycleRegistry(merger)

            lifecycle.forEach { it.addObserver(merger) }

            return merger
        }
    }
}