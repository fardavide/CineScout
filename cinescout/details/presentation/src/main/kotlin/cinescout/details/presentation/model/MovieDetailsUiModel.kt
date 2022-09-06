package cinescout.details.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class MovieDetailsUiModel(
    val title: String,
    val tmdbId: TmdbMovieId
)
