package cinescout.utils.kotlin

import java.util.Locale

fun Double.format(digits: Int) = String.format(Locale.getDefault(), "%.${digits}f", this)
