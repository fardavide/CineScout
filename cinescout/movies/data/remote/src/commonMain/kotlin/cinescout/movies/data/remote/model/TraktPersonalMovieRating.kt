package cinescout.movies.data.remote.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Rating

data class TraktPersonalMovieRating(
    val tmdbId: TmdbMovieId,
    val rating: Rating
)
