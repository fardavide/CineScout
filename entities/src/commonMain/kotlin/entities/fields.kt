package entities

import com.soywiz.klock.DateTime

data class Actor(
    val id: TmdbId,
    val name: Name
)

data class CommunityRating(
    val average: Double,
    val count: UInt
)

data class FiveYearRange internal constructor (val range: UIntRange) {
    constructor(end: UInt) : this(end - RANGE .. end)

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

data class Poster(val baseUrl: String, val path: String) {

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
