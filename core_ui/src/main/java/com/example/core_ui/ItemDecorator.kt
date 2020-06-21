package com.example.core_ui

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(
    @Px private val offsetLeft: Int = DEFAULT_OFFSET,
    @Px private val offsetTop: Int = DEFAULT_OFFSET,
    @Px private val offsetRight: Int = DEFAULT_OFFSET,
    @Px private val offsetBottom: Int = DEFAULT_OFFSET
) : RecyclerView.ItemDecoration() {

    constructor(@Px offset: Int) : this(offset, offset, offset, offset)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(offsetLeft, offsetTop, offsetRight, offsetBottom)
    }

    companion object {
        private val DEFAULT_OFFSET = dpToPx(8)
    }
}