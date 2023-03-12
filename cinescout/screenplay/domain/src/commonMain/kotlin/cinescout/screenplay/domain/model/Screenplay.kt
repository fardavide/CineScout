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
    override val title: String,
    override val tmdbId: TmdbScreenplayId.TvShow
) : Screenplay



fun List<Screenplay>.ids(): List<TmdbScreenplayId> = map { it.tmdbId }

fun Flow<List<Screenplay>>.ids(): Flow<List<TmdbScreenplayId>> = map { movies -> movies.ids() }
