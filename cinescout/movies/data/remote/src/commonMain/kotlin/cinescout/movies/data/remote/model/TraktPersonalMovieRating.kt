package cinescout.movies.data.remote.model

import cinescout.common.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

data class TraktPersonalMovieRating(
    val tmdbId: TmdbMovieId,
    val rating: Rating
)
