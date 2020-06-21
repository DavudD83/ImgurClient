package com.example.core_ui

import android.view.View
import android.view.WindowInsets

fun <V : View> V.doOnApplyInsets(onApplyInsets: (V, WindowInsets) -> WindowInsets) {
    fun View.applyInsetsWhenAttached() {
        if (isAttachedToWindow) {
            requestApplyInsets()
        } else {
            addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    v.requestApplyInsets()
                }

                override fun onViewDetachedFromWindow(v: View?) {}
            })
        }
    }

    setOnApplyWindowInsetsListener { v, insets ->
        onApplyInsets(v as V, insets)
    }

    applyInsetsWhenAttached()
}