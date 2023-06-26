package cinescout.screenplay.domain.model

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import arrow.optics.optics
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TmdbTvShowId
import cinescout.screenplay.domain.model.id.TraktMovieId
import cinescout.screenplay.domain.model.id.TraktScreenplayId
import cinescout.screenplay.domain.model.id.TraktTvShowId
import cinescout.screenplay.domain.model.id.TvShowIds
import korlibs.time.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration

@optics sealed interface Screenplay {

    val ids: ScreenplayIds
    val overview: String
    val rating: PublicRating
    val relevantDate: Option<Date>
    val runtime: Option<Duration>
    val tagline: Option<String>
    val title: String

    val tmdbId: TmdbScreenplayId get() = ids.tmdb
    val traktId: TraktScreenplayId get() = ids.trakt

    companion object
}

@optics data class Movie(
    override val ids: MovieIds,
    override val overview: String,
    override val rating: PublicRating,
    val releaseDate: Option<Date>,
    override val runtime: Option<Duration>,
    override val tagline: Option<String>,
    override val title: String
) : Screenplay {

    override val relevantDate: Option<Date>
        get() = releaseDate

    override val tmdbId: TmdbMovieId
        get() = ids.tmdb

    override val traktId: TraktMovieId
        get() = ids.trakt

    companion object
}

@optics data class TvShow(
    val airedEpisodes: Int,
    val firstAirDate: Date,
    override val ids: TvShowIds,
    override val overview: String,
    override val rating: PublicRating,
    override val runtime: Option<Duration>,
    val status: TvShowStatus,
    override val title: String
) : Screenplay {

    override val relevantDate: Option<Date>
        get() = firstAirDate.some()

    override val tagline: Option<String> = none()

    override val tmdbId: TmdbTvShowId
        get() = ids.tmdb

    override val traktId: TraktTvShowId
        get() = ids.trakt

    companion object
}

@JvmName("ids")
fun List<Screenplay>.ids(): List<ScreenplayIds> = map { it.ids }

@JvmName("movie_ids")
fun List<Movie>.ids(): List<TmdbMovieId> = map { it.tmdbId }

@JvmName("tv_show_ids")
fun List<TvShow>.ids(): List<TmdbTvShowId> = map { it.tmdbId }

@JvmName("ids")
fun Flow<List<Screenplay>>.ids(): Flow<List<ScreenplayIds>> = map { screenplays -> screenplays.ids() }

@JvmName("movie_ids")
fun Flow<List<Movie>>.ids(): Flow<List<TmdbMovieId>> = map { movies -> movies.ids() }

@JvmName("tv_show_ids")
fun Flow<List<TvShow>>.ids(): Flow<List<TmdbTvShowId>> = map { tvShows -> tvShows.ids() }

@JvmName("tmdbIds")
fun List<Screenplay>.tmdbIds(): List<TmdbScreenplayId> = map { it.tmdbId }

@JvmName("tmdbIds")
fun Flow<List<Screenplay>>.tmdbIds(): Flow<List<TmdbScreenplayId>> =
    map { screenplays -> screenplays.tmdbIds() }

fun List<Screenplay>.filterByType(type: ScreenplayTypeFilter): List<Screenplay> = when (type) {
    ScreenplayTypeFilter.All -> this
    ScreenplayTypeFilter.Movies -> filterIsInstance<Movie>()
    ScreenplayTypeFilter.TvShows -> filterIsInstance<TvShow>()
}
