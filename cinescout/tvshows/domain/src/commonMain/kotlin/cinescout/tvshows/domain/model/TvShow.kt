package cinescout.tvshows.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import com.soywiz.klock.Date

data class TvShow(
    val backdropImage: Option<TmdbBackdropImage>,
    val firstAirDate: Date,
    val overview: String,
    val posterImage: Option<TmdbPosterImage>,
    val rating: PublicRating,
    val title: String,
    val tmdbId: TmdbTvShowId
)
