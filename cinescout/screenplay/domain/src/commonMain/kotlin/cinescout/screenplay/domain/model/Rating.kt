package cinescout.screenplay.domain.model

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right

@JvmInline
value class Rating private constructor(val value: Double) {
    
    val intValue: Int
        get() = value.toInt()

    companion object {

        fun of(value: Double): Either<Double, Rating> =
            if (value in 0.0..10.0) Rating(value).right() else value.left()

        fun of(value: Int): Either<Double, Rating> = of(value.toDouble())
    }
}

fun Either<Double, Rating>.getOrThrow(): Rating =
    getOrElse { throw IllegalArgumentException("Invalid rating: $this") }
