package cinescout.suggestions.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

data class ForYouScreenplayUiModel(
    val tmdbScreenplayId: TmdbScreenplayId,
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
