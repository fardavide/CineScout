package cinescout.movies.domain.model

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.Validated

@JvmInline
value class Rating private constructor(val value: Int) {

    companion object {

        fun of(value: Int): Validated<Unit, Rating> =
            if (value in 0..10) Valid(Rating(value)) else Invalid(Unit)
    }
}
