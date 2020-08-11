import com.soywiz.klock.DateTime

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

inline class Name(val s: String)

enum class Rating(val weight: Int) { Positive(1), Negative(-1) }
