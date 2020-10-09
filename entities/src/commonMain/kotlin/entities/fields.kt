@file:Suppress("DataClassPrivateConstructor")

package entities

import com.soywiz.klock.DateTime
import entities.Validable.Companion.validate

data class Actor(
    val id: TmdbId,
    val name: Name
)

data class CommunityRating(
    val average: Double,
    val count: UInt
)

/**
 * Entity representing an email address
 * [Validable] by [RegexValidator]
 */
data class EmailAddress private constructor(val s: String) :
    Validable<RegexMismatchError> by RegexValidator(::EmailAddress, s, VALIDATION_REGEX) {

    companion object {

        operator fun invoke(s: String) = EmailAddress(s).validate()

        @Suppress("MaxLineLength") // Nobody can read it anyway ¯\_(ツ)_/¯
        const val VALIDATION_PATTERN =
            """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"""
        val VALIDATION_REGEX = VALIDATION_PATTERN.toRegex(RegexOption.IGNORE_CASE)
    }
}

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

data class ImageUrl(val baseUrl: String, val path: String) {

    fun get(size: Size): String =
        "${baseUrl.trimEnd('/')}/${size.name.toLowerCase()}/${path.trimStart('/')}"

    enum class Size {
        W92,
        W154,
        W185,
        W342,
        W500,
        W780,
        Original
    }
}

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
