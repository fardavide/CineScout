package cinescout.lists.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class ListItemUiModel(
    val personalRating: String?,
    val posterUrl: String?,
    val rating: String,
    val title: String,
    val tmdbId: TmdbMovieId
)
