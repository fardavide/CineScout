package cinescout.movies.domain.model

import arrow.core.Option
import com.soywiz.klock.Date

data class Movie(
    val backdropImage: Option<TmdbBackdropImage>,
    val posterImage: TmdbPosterImage,
    val rating: MovieRating,
    val releaseDate: Date,
    val title: String,
    val tmdbId: TmdbMovieId
)
