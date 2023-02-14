package cinescout.details.presentation.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.collections.immutable.ImmutableList

data class TvShowDetailsUiModel(
    val creditsMember: ImmutableList<CreditsMember>,
    val genres: ImmutableList<String>,
    val backdrops: ImmutableList<String?>,
    val firstAirDate: String,
    val isInWatchlist: Boolean,
    val overview: String,
    val posterUrl: String?,
    val ratings: ScreenPlayRatingsUiModel,
    val title: String,
    val tmdbId: TmdbTvShowId,
    val videos: ImmutableList<Video>
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
