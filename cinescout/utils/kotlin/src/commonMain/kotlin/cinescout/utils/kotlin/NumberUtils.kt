package cinescout.utils.kotlin

import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.format(digits: Int) = String.format(Locale.getDefault(), "%.${digits}f", this)

fun Number.roundToDecimals(decimals: Int): Double {
    val factor = 10.0.pow(decimals.toDouble())
    return (toDouble() * factor).roundToInt() / factor
}
