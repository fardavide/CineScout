package cinescout.screenplay.domain.model

import arrow.core.Option
import com.soywiz.klock.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface Screenplay {

    val title: String
    val tmdbId: TmdbScreenplayId
}

data class Movie(
    val backdropImage: Option<TmdbBackdropImage>,
    val overview: String,
    val posterImage: Option<TmdbPosterImage>,
    val rating: PublicRating,
    val releaseDate: Option<Date>,
    override val title: String,
    override val tmdbId: TmdbScreenplayId.Movie
) : Screenplay

data class TvShow(
    val backdropImage: Option<TmdbBackdropImage>,
    val firstAirDate: Date,
    val overview: String,
    val posterImage: Option<TmdbPosterImage>,
    val rating: PublicRating,
    override val title: String,
    override val tmdbId: TmdbScreenplayId.TvShow
) : Screenplay

@JvmName("ids")
fun List<Screenplay>.ids(): List<TmdbScreenplayId> = map { it.tmdbId }

@JvmName("movie_ids")
fun List<Movie>.ids(): List<TmdbScreenplayId.Movie> = map { it.tmdbId }

@JvmName("tv_show_ids")
fun List<TvShow>.ids(): List<TmdbScreenplayId.TvShow> = map { it.tmdbId }

@JvmName("ids")
fun Flow<List<Screenplay>>.ids(): Flow<List<TmdbScreenplayId>> = map { screenplays -> screenplays.ids() }

@JvmName("movie_ids")
fun Flow<List<Movie>>.ids(): Flow<List<TmdbScreenplayId.Movie>> = map { movies -> movies.ids() }

@JvmName("tv_show_ids")
fun Flow<List<TvShow>>.ids(): Flow<List<TmdbScreenplayId.TvShow>> = map { tvShows -> tvShows.ids() }
