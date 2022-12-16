package cinescout.design.util

import androidx.compose.ui.graphics.Color

fun Color.Companion.blend(
    color1: Color,
    color2: Color,
    ratio: Float = 0.5f
): Color {
    val inverseRatio = 1f - ratio
    val r = color1.red * ratio + color2.red * inverseRatio
    val g = color1.green * ratio + color2.green * inverseRatio
    val b = color1.blue * ratio + color2.blue * inverseRatio
    val a = color1.alpha * ratio + color2.alpha * inverseRatio
    return Color(red = r, green = g, blue = b, alpha = a)
}
