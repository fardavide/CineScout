package cinescout.movies.domain.model

import arrow.core.Option

data class MovieWithExtras(
    val movieWithDetails: MovieWithDetails,
    val credits: MovieCredits,
    val keywords: MovieKeywords,
    val personalRating: Option<Rating>
)
