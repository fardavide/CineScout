package cinescout.movies.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import com.soywiz.klock.Date

data class Movie(
    val backdropImage: Option<TmdbBackdropImage>,
    val overview: String,
    val posterImage: Option<TmdbPosterImage>,
    val rating: PublicRating,
    val releaseDate: Option<Date>,
    val title: String,
    val tmdbId: TmdbMovieId
)
