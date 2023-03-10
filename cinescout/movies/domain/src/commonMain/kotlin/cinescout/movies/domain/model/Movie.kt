package cinescout.movies.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import com.soywiz.klock.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Movie(
    val backdropImage: Option<TmdbBackdropImage>,
    val overview: String,
    val posterImage: Option<TmdbPosterImage>,
    val rating: PublicRating,
    val releaseDate: Option<Date>,
    val title: String,
    val tmdbId: TmdbMovieId
)

fun List<Movie>.ids(): List<TmdbMovieId> = map { it.tmdbId }

fun Flow<List<Movie>>.ids(): Flow<List<TmdbMovieId>> = map { movies -> movies.ids() }
