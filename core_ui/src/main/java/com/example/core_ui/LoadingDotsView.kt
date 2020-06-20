package com.example.core_ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import kotlin.math.roundToInt

class LoadingDotsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val defaultColor = resources.getColor(R.color.black)
    private val dotColor: Int

    private val dotsCount: Int
    private val dotSize: Int

    private val defaultDotSize = dpToPx(10)
    private val defaultDotMargins = dpToPx(3)

    private val desiredWidth: Int
    private val desiredHeight: Int

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        context.obtainStyledAttributes(attributeSet, R.styleable.LoadingDotsView).apply {
            dotColor = getColor(R.styleable.LoadingDotsView_dotsColor, defaultColor)
            dotsCount = getInt(R.styleable.LoadingDotsView_dotsCount, DEFAULT_DOTS_COUNT)
            dotSize = getDimensionPixelSize(R.styleable.LoadingDotsView_dotSize, defaultDotSize)

            recycle()
        }

        desiredHeight = (dotSize * DEFAULT_SCALE + defaultDotMargins * 2).roundToInt()
        desiredWidth = ((dotSize * DEFAULT_SCALE + defaultDotMargins * 2) * dotsCount).roundToInt()

        initDots()
        startAnimation()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //measure children
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //setup our view to be with size of dots plus space to scale
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    private fun initDots() {
        val dotShape = OvalShape()
        val dotDrawable = ShapeDrawable(dotShape)

        dotDrawable.paint.run {
            style = Paint.Style.FILL
            color = dotColor
        }

        repeat(dotsCount) {
            val view = View(context)
            view.background = dotDrawable
            view.layoutParams = MarginLayoutParams(
                dotSize,
                dotSize
            ).apply {
                val marginVert =
                    ((dotSize * DEFAULT_SCALE) - dotSize / 2 + defaultDotMargins).roundToInt()
                setMargins(defaultDotMargins, marginVert, defaultDotMargins, marginVert)
            }
            view.alpha = START_ALPHA

            addView(view)
        }
    }

    private fun startAnimation() {
        children.forEachIndexed { index, view ->
            val delay = SCALE_ANIMATION_DURATION / dotsCount * index

            val scaleAnimator = ValueAnimator.ofFloat(1f, DEFAULT_SCALE).apply {
                duration = SCALE_ANIMATION_DURATION
                startDelay = delay
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener {
                    view.scaleX = it.animatedValue as Float
                    view.scaleY = it.animatedValue as Float
                }
            }
            val alphaAnimator = ValueAnimator.ofFloat(view.alpha, 1f).apply {
                duration = SCALE_ANIMATION_DURATION
                startDelay = delay
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener {
                    view.alpha = it.animatedValue as Float
                }
            }

            scaleAnimator.start()
            alphaAnimator.start()
        }
    }

    companion object {
        private const val START_ALPHA = 0.7f
        private const val SCALE_ANIMATION_DURATION = 400L
        private const val DEFAULT_DOTS_COUNT = 3
        private const val DEFAULT_SCALE = 1.3f
    }
}