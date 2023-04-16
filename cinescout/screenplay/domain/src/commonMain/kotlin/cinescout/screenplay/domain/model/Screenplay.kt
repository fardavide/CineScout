package cinescout.screenplay.domain.model

import arrow.core.Option
import arrow.core.some
import com.soywiz.klock.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed interface Screenplay {

    val ids: ScreenplayIds
    val overview: String
    val rating: PublicRating
    val relevantDate: Option<Date>
    val title: String

    val tmdbId: TmdbScreenplayId get() = ids.tmdb
    val traktId: TraktScreenplayId get() = ids.trakt
}

data class Movie(
    override val ids: ScreenplayIds.Movie,
    override val overview: String,
    override val rating: PublicRating,
    val releaseDate: Option<Date>,
    override val title: String
) : Screenplay {

    override val relevantDate: Option<Date>
        get() = releaseDate

    override val tmdbId: TmdbScreenplayId.Movie
        get() = ids.tmdb

    override val traktId: TraktScreenplayId.Movie
        get() = ids.trakt
}

data class TvShow(
    val firstAirDate: Date,
    override val ids: ScreenplayIds.TvShow,
    override val overview: String,
    override val rating: PublicRating,
    override val title: String
) : Screenplay {

    override val relevantDate: Option<Date>
        get() = firstAirDate.some()

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = ids.tmdb

    override val traktId: TraktScreenplayId.TvShow
        get() = ids.trakt
}

@JvmName("ids")
fun List<Screenplay>.ids(): List<ScreenplayIds> = map { it.ids }

@JvmName("movie_ids")
fun List<Movie>.ids(): List<TmdbScreenplayId.Movie> = map { it.tmdbId }

@JvmName("tv_show_ids")
fun List<TvShow>.ids(): List<TmdbScreenplayId.TvShow> = map { it.tmdbId }

@JvmName("ids")
fun Flow<List<Screenplay>>.ids(): Flow<List<ScreenplayIds>> = map { screenplays -> screenplays.ids() }

@JvmName("movie_ids")
fun Flow<List<Movie>>.ids(): Flow<List<TmdbScreenplayId.Movie>> = map { movies -> movies.ids() }

@JvmName("tv_show_ids")
fun Flow<List<TvShow>>.ids(): Flow<List<TmdbScreenplayId.TvShow>> = map { tvShows -> tvShows.ids() }

@JvmName("tmdbIds")
fun List<Screenplay>.tmdbIds(): List<TmdbScreenplayId> = map { it.tmdbId }

@JvmName("tmdbIds")
fun Flow<List<Screenplay>>.tmdbIds(): Flow<List<TmdbScreenplayId>> =
    map { screenplays -> screenplays.tmdbIds() }

fun List<Screenplay>.filterByType(type: ScreenplayType): List<Screenplay> = when (type) {
    ScreenplayType.All -> this
    ScreenplayType.Movies -> filterIsInstance<Movie>()
    ScreenplayType.TvShows -> filterIsInstance<TvShow>()
}
