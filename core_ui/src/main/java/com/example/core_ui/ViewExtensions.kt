package com.example.core_ui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

fun <V : View> V.doOnApplyInsets(onApplyInsets: (V, WindowInsets) -> WindowInsets) {
    fun View.applyInsetsWhenAttached() {
        if (isAttachedToWindow) {
            requestApplyInsets()
        } else {
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
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

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    url: String?,
    @DrawableRes loadingPlaceholder: Int? = null,
    @DrawableRes errorPlaceholder: Int? = null,
    applyCircleCrop: Boolean = false,
    skipCache: Boolean = false
) {
    url?.let { urlNotNull ->
        val requestBuilder = Glide.with(this)
            .load(url)
        loadingPlaceholder?.let(requestBuilder::placeholder)
        errorPlaceholder?.let(requestBuilder::error)
        if (applyCircleCrop) {
            requestBuilder.circleCrop()
        }
        if (skipCache) {
            requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        requestBuilder.into(this)
    }
}

fun ViewGroup.showSnackbar(
    text: String,
    @BaseTransientBottomBar.Duration duration: Int = LENGTH_SHORT
) {
    Snackbar.make(this, text, duration)
        .setTextColor(ContextCompat.getColor(context, R.color.snackbar_text_color))
        .show()
}