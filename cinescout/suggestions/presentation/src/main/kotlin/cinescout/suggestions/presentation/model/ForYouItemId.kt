package cinescout.suggestions.presentation.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId

sealed interface ForYouItemId {

    data class Movie(val tmdbMovieId: TmdbMovieId) : ForYouItemId
    data class TvShow(val tmdbTvShowId: TmdbTvShowId) : ForYouItemId
}
