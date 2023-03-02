package cinescout.movies.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.Rating

data class MovieWithExtras(
    val movieWithDetails: MovieWithDetails,
    val credits: MovieCredits,
    val isInWatchlist: Boolean,
    val keywords: MovieKeywords,
    val personalRating: Option<Rating>
)
