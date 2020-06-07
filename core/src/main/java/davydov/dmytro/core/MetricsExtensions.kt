package davydov.dmytro.core

import android.content.res.Resources
import kotlin.math.roundToInt

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()
}