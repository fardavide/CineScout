package cinescout.details.presentation.model

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.common.model.Rating
import cinescout.tvshows.domain.model.TmdbTvShowId

data class TvShowDetailsUiModel(
    val creditsMember: List<CreditsMember>,
    val genres: List<String>,
    val backdrops: List<String?>,
    val firstAirDate: String,
    val isInWatchlist: Boolean,
    val overview: String,
    val posterUrl: String?,
    val ratings: Ratings,
    val title: String,
    val tmdbId: TmdbTvShowId,
    val videos: List<Video>
) {

    data class CreditsMember(
        val name: String,
        val profileImageUrl: String?,
        val role: String?
    )

    data class Ratings(
        val publicAverage: String,
        val publicCount: String,
        val personal: Personal
    ) {

        sealed class Personal(open val rating: Option<Rating>) {

            data class Rated(override val rating: Option<Rating>, val stringValue: String) : Personal(rating) {
                constructor(rating: Rating, stringValue: String) : this(rating.some(), stringValue)
            }

            object NotRated : Personal(none())
        }
    }

    data class Video(
        val previewUrl: String,
        val title: String,
        val url: String
    )
}
