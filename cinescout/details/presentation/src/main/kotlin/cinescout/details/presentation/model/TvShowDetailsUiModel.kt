package cinescout.details.presentation.model

import cinescout.tvshows.domain.model.TmdbTvShowId

data class TvShowDetailsUiModel(
    val creditsMember: List<CreditsMember>,
    val genres: List<String>,
    val backdrops: List<String?>,
    val firstAirDate: String,
    val isInWatchlist: Boolean,
    val overview: String,
    val posterUrl: String?,
    val ratings: ScreenPlayRatingsUiModel,
    val title: String,
    val tmdbId: TmdbTvShowId,
    val videos: List<Video>
) {

    data class CreditsMember(
        val name: String,
        val profileImageUrl: String?,
        val role: String?
    )

    data class Video(
        val previewUrl: String,
        val title: String,
        val url: String
    )
}
