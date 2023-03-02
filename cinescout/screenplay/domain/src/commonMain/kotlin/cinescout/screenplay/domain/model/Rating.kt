package cinescout.screenplay.domain.model

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import arrow.core.getOrElse

@JvmInline
value class Rating private constructor(val value: Double) {
    
    val intValue: Int
        get() = value.toInt()

    companion object {

        fun of(value: Double): Validated<Double, Rating> =
            if (value in 0.0..10.0) Valid(Rating(value)) else Invalid(value)

        fun of(value: Int): Validated<Double, Rating> = of(value.toDouble())
    }
}

fun Validated<Double, Rating>.getOrThrow(): Rating =
    getOrElse { throw IllegalArgumentException("Invalid rating: $this") }
