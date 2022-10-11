package cinescout.movies.domain.model

import cinescout.common.model.Rating
import kotlin.math.roundToInt

data class MovieRating(
    val voteCount: Int,
    val average: Rating
) {

    override fun equals(other: Any?) =
        other is MovieRating && hashCode() == other.hashCode()

    override fun hashCode() =
        voteCount / 100 + (average.value * 10).roundToInt()
}
