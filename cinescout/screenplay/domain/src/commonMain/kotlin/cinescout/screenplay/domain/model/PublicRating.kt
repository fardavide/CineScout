package cinescout.screenplay.domain.model

import arrow.optics.optics
import kotlin.math.roundToInt

@optics data class PublicRating(
    val voteCount: Int,
    val average: Rating
) {

    override fun equals(other: Any?) = other is PublicRating && hashCode() == other.hashCode()

    override fun hashCode() = voteCount / 100 + (average.value * 10).roundToInt()

    companion object
}
