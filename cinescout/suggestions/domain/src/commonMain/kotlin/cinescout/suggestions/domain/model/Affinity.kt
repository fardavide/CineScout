package cinescout.suggestions.domain.model

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated
import arrow.core.getOrElse

@JvmInline
value class Affinity private constructor(val value: Double) {

    companion object {

        fun of(value: Double): Validated<Double, Affinity> =
            if (value in 0.0..100.0) Valid(Affinity(value)) else Invalid(value)

        fun of(value: Int): Validated<Double, Affinity> =
            of(value.toDouble())
    }
}

fun Validated<Double, Affinity>.getOrThrow(): Affinity =
    getOrElse { throw IllegalArgumentException("Invalid affinity: $this") }
