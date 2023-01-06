package cinescout.details.presentation.model

import cinescout.movies.domain.model.TmdbMovieId

data class MovieDetailsUiModel(
    val creditsMember: List<CreditsMember>,
    val genres: List<String>,
    val backdrops: List<String?>,
    val isInWatchlist: Boolean,
    val overview: String,
    val posterUrl: String?,
    val ratings: ScreenPlayRatingsUiModel,
    val releaseDate: String,
    val title: String,
    val tmdbId: TmdbMovieId,
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
