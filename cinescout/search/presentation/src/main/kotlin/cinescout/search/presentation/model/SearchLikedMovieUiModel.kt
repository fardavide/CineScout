package cinescout.search.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class SearchLikedMovieUiModel(
    val movieId: TmdbMovieId,
    val posterUrl: String?,
    val title: String
)
