package cinescout.lists.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class WatchlistItemUiModel(
    val posterUrl: String?,
    val title: String,
    val tmdbId: TmdbMovieId
)
