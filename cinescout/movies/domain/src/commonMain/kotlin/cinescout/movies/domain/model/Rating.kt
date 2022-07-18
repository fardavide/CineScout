package cinescout.movies.domain.model

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated

@JvmInline
value class Rating private constructor(val value: Double) {

    companion object {

        fun of(value: Double): Validated<Unit, Rating> =
            if (value in 0.0..10.0) Valid(Rating(value)) else Invalid(Unit)

        fun of(value: Int): Validated<Unit, Rating> =
            of(value.toDouble())
    }
}
