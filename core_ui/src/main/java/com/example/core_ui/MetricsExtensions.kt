package com.example.core_ui

import android.content.res.Resources
import kotlin.math.roundToInt

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()
}