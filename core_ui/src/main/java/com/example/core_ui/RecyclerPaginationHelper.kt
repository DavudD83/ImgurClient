package com.example.core_ui

import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class RecyclerPaginationHelper @Inject constructor() : LifecycleObserver {

    private var recyclerView: RecyclerView? = null
    private var onNewPage: (() -> Unit)? = null

    private val onScrollListener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val verticalScrollDown = dy > 0

            if (verticalScrollDown) {
                val lastChild = recyclerView.children.last()
                val lastViewAdapterPos = (lastChild.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

                if (lastViewAdapterPos == recyclerView.adapter!!.itemCount - 1) {
                    onNewPage?.invoke()
                }
            }
        }
    }

    fun subscribe(recyclerView: RecyclerView, owner: LifecycleOwner, onNewPage: () -> Unit) {
        this.recyclerView = recyclerView
        this.onNewPage = onNewPage
        owner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onActive() {
        recyclerView!!.addOnScrollListener(onScrollListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onInactive() {
        recyclerView!!.removeOnScrollListener(onScrollListener)
        recyclerView = null
    }
}