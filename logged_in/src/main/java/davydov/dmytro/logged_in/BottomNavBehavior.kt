package davydov.dmytro.logged_in

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.abs

class BottomNavBehavior(context: Context? = null, attributeSet: AttributeSet? = null) :
    CoordinatorLayout.Behavior<BottomNavigationView>(context, attributeSet) {

    private var state = State.SHOWN

    private var currentAnimator: ViewPropertyAnimator? = null

    private var scrollDyConsumed: Int = 0

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return target is RecyclerView && axes == CoordinatorLayout.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        currentAnimator?.cancel()
        child.clearAnimation()

        scrollDyConsumed += dyConsumed

        val maxTranslationY = child.height.toFloat()

        val newTranslationY =
            (child.translationY + dyConsumed).coerceIn(MIN_TRANSLATION_Y, maxTranslationY)

        child.translationY = newTranslationY
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        target: View,
        type: Int
    ) {
        if (scrollDyConsumed == 0) {
            return
        }

        val currentTranslationY = child.translationY

        val maxTranslationY = child.height.toFloat()

        val minDyForTransition = maxTranslationY * TRANSITION_HEIGHT_FRACTION

        if (abs(scrollDyConsumed) <= minDyForTransition && currentTranslationY != MIN_TRANSLATION_Y && currentTranslationY != maxTranslationY) {
            val returnScroll = -scrollDyConsumed
            (target as? RecyclerView)?.smoothScrollBy(0, returnScroll)
        }

        scrollDyConsumed = 0

        when (state) {
            State.SHOWN -> {
                if (currentTranslationY > minDyForTransition) {
                    animOut(child)
                } else {
                    animIn(child)
                }
            }
            State.HIDDEN -> {
                val animTriggerTranslation = maxTranslationY - minDyForTransition
                if (currentTranslationY < animTriggerTranslation) {
                    animIn(child)
                } else {
                    animOut(child)
                }
            }
        }
    }

    private fun animOut(view: BottomNavigationView) {
        animView(view, view.height.toFloat(), State.HIDDEN)
    }

    private fun animIn(view: BottomNavigationView) {
        animView(view, 0f, State.SHOWN)
    }

    private fun animView(view: BottomNavigationView, translationY: Float, newState: State) {
        currentAnimator?.cancel()
        view.clearAnimation()

        currentAnimator = view
            .animate()
            .translationY(translationY)
            .setInterpolator(FastOutSlowInInterpolator())
            .withEndAction {
                currentAnimator = null
                state = newState
            }
    }

    enum class State {
        HIDDEN, SHOWN
    }

    companion object {
        private const val MIN_TRANSLATION_Y = 0f
        private const val TRANSITION_HEIGHT_FRACTION = 0.3f
    }
}