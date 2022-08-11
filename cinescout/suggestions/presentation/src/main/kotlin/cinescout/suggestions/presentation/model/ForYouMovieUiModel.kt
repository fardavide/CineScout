package cinescout.suggestions.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class ForYouMovieUiModel(
    val tmdbMovieId: TmdbMovieId,
    val actors: List<Actor>,
    val backdropUrl: String?,
    val genres: List<String>,
    val posterUrl: String?,
    val rating: String,
    val releaseYear: String,
    val title: String
) {

    data class Actor(
        val profileImageUrl: String?
    )
}
