package com.example.core_ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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

    private val defaultDotSize = dpToPx(12)
    private val defaultDotMargins = dpToPx(4)

    private val desiredWidth: Int
    private val desiredHeight: Int

    private val animators = mutableListOf<Animator>()

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LoadingDotsView)

        try {
            typedArray.run {
                dotColor = getColor(R.styleable.LoadingDotsView_dotsColor, defaultColor)
                dotsCount = getInt(R.styleable.LoadingDotsView_dotsCount, DEFAULT_DOTS_COUNT)
                dotSize = getDimensionPixelSize(R.styleable.LoadingDotsView_dotSize, defaultDotSize)
            }
        } finally {
            typedArray.recycle()
        }

        desiredHeight = (dotSize * DEFAULT_SCALE + defaultDotMargins * 2).roundToInt()
        desiredWidth = ((dotSize * DEFAULT_SCALE + defaultDotMargins * 2) * dotsCount).roundToInt()

        initDots()
        initAnimators()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
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
        animators.forEach { it.start() }
    }

    private fun stopAnimation() {
        animators.forEach { it.cancel() }
    }

    private fun initAnimators() {
        children.forEachIndexed { index, view ->
            val delay = SCALE_ANIMATION_DURATION / dotsCount * index

            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, DEFAULT_SCALE)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, DEFAULT_SCALE)

            val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
                duration = SCALE_ANIMATION_DURATION
                startDelay = delay
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
            }

            val alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, START_ALPHA, 1f).apply {
                duration = SCALE_ANIMATION_DURATION
                startDelay = delay
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
            }

            animators += scaleAnimator
            animators += alphaAnimator
        }
    }

    companion object {
        private const val START_ALPHA = 0.7f
        private const val SCALE_ANIMATION_DURATION = 400L
        private const val DEFAULT_DOTS_COUNT = 3
        private const val DEFAULT_SCALE = 1.3f
    }
}