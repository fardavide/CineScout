package cinescout.details.presentation.model

import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.collections.immutable.ImmutableList

data class MovieDetailsUiModel(
    val creditsMember: ImmutableList<CreditsMember>,
    val genres: ImmutableList<String>,
    val backdrops: ImmutableList<String?>,
    val isInWatchlist: Boolean,
    val overview: String,
    val posterUrl: String?,
    val ratings: ScreenPlayRatingsUiModel,
    val releaseDate: String,
    val title: String,
    val tmdbId: TmdbMovieId,
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
