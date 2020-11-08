@file:Suppress("DataClassPrivateConstructor")

package entities.field

import com.soywiz.klock.DateTime
import entities.BlankStringError
import entities.Either
import entities.NotBlankStringValidator
import entities.TmdbId
import entities.TmdbStringId
import entities.Validable
import entities.Validable.Companion.validate
import entities.ValidationError
import entities.Validator
import entities.left
import entities.right

data class Actor(
    val id: TmdbId,
    val name: Name
)

data class CommunityRating(
    val average: Double,
    val count: UInt
)



data class FiveYearRange internal constructor(val range: UIntRange) {
    constructor(end: UInt) : this(end - RANGE..end)

    companion object {

        operator fun invoke(forYear: UInt): FiveYearRange {
            val current = DateTime.now().yearInt.toUInt()
            val year = when {
                current > forYear -> current.decreaseUntilCloserTo(forYear)
                current < forYear -> current.increaseUntilGreaterThan(forYear)
                else -> current
            }
            return FiveYearRange(end = year)
        }

        private fun UInt.decreaseUntilCloserTo(forYear: UInt): UInt {
            var result = this
            while (result > forYear) {
                result -= RANGE
            }
            return result + RANGE
        }

        private fun UInt.increaseUntilGreaterThan(forYear: UInt): UInt {
            var result = this
            while (result < forYear) {
                result += RANGE
            }
            return result
        }

        private const val RANGE = 5u
    }

    init {
        require(range.first == range.last - RANGE) {
            "Range should be of 5 years"
        }
    }
}

data class Genre(
    val id: TmdbId,
    val name: Name
)

inline class Name(val s: String)

/**
 * Entity representing a generic String that cannot be blank
 * [Validable] by [NotBlankStringValidator]
 */
data class NotBlankString private constructor(val s: String) :
    Validable<BlankStringError> by NotBlankStringValidator(::NotBlankString, s) {

    companion object {

        operator fun invoke(s: String) = NotBlankString(s).validate()
    }
}

sealed class InvalidPasswordError : ValidationError {
    object EmptyPasswordError : InvalidPasswordError()
    object ShortPasswordError : InvalidPasswordError()
}

/**
 * Entity representing a Password that can be validated by its format
 */
data class Password private constructor(val s: String) :
    Validable<InvalidPasswordError> by (Validator {
        when {
            s.isBlank() -> InvalidPasswordError.EmptyPasswordError.left()
            s.length < 6 -> InvalidPasswordError.ShortPasswordError.left()
            else -> Password(s).right()
        }
    }) {

    companion object {
        operator fun invoke(s: String) = Password(s).validate()
    }
}

typealias Either_Password = Either<InvalidPasswordError, Password>

enum class UserRating(val weight: Int) { Positive(1), Neutral(0), Negative(-1);

    companion object {
        operator fun invoke(weight: Int): UserRating =
            values().find { it.weight == weight }
                ?: throw IllegalArgumentException("Unexpected weight: $weight")
    }
}

data class Video(
    val id: TmdbStringId,
    val title: Name,
    val site: Site,
    val key: String,
    val type: Type,
    val size: UInt,
) {

    val url = "${site.baseUrl}$key"

    enum class Site(val baseUrl: String) {
        YouTube("https://www.youtube.com/watch?v=")
    }

    enum class Type { Clip, Featurette, Teaser, Trailer }
}
