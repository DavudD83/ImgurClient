package davydov.dmytro.feature_account.posts.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import davydov.dmytro.feature_account.R
import davydov.dmytro.feature_account.posts.ui.PostsFragment.Companion.POSTS_COUNT_IN_ROW

class PostsItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val offset = context.resources.getDimensionPixelSize(R.dimen.item_post_margin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val firstRow =
            itemPosition in 0 until POSTS_COUNT_IN_ROW
        val top = if (firstRow) {
            offset
        } else {
            0
        }
        val itemCount = (parent.adapter?.itemCount ?: 0)
        val lastRow = itemPosition in (itemCount - POSTS_COUNT_IN_ROW) until itemCount
        val bottom = if (lastRow) {
            offset
        } else {
            0
        }
        val left = if (itemPosition % 2 == 0) {
            offset
        } else {
            0
        }
        val right = if (itemPosition % 2 == 0) {
            0
        } else {
            offset
        }
        outRect.set(left, top, right, bottom)
    }
}