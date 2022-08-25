package cinescout.lists.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class WatchlistItemUiModel(
    val tmdbId: TmdbMovieId,
    val title: String
)
