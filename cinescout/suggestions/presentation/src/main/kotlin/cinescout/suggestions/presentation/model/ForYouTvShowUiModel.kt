package cinescout.suggestions.presentation.model

import cinescout.tvshows.domain.model.TmdbTvShowId

data class ForYouTvShowUiModel(
    val tmdbTvShowId: TmdbTvShowId,
    val actors: List<Actor>,
    val backdropUrl: String?,
    val firstAirDate: String,
    val genres: List<String>,
    val posterUrl: String?,
    val rating: String,
    val title: String
) {

    data class Actor(
        val profileImageUrl: String?
    )
}
