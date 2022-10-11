package cinescout.tvshows.domain.model

import arrow.core.Option
import cinescout.common.model.PublicRating
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import com.soywiz.klock.Date

data class TvShow(
    val backdropImage: Option<TmdbBackdropImage>,
    val overview: String,
    val posterImage: Option<TmdbPosterImage>,
    val rating: PublicRating,
    val firstAirData: Date,
    val title: String,
    val tmdbId: TmdbTvShowId
)
