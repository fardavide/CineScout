package cinescout.model

import cinescout.utils.kotlin.roundToDecimals
import kotlin.math.roundToInt

@JvmInline
value class Percent internal constructor(val value: Double) {

    fun roundToInt() = value.roundToInt()

    companion object {

        fun of(value: Number, total: Number): Percent {
            val percentDouble = (value.toDouble() / total.toDouble() * 100.0)
                .coerceIn(minimumValue = 0.0, maximumValue = 100.0)
            return percentDouble.percent
        }
    }
}

val Number.percent: Percent
    get() {
        val double = roundToDecimals(decimals = 2)
        check(double in 0.0..100.0) { "Invalid percent: $this, expected 0.0..100.0" }
        return Percent(double)
    }
