package cinescout.movies.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Deprecated(
    "Use cinescout.screenplay.domain.model.Movie instead",
    ReplaceWith("Movie", "cinescout.screenplay.domain.model.Movie")
)
typealias Movie = cinescout.screenplay.domain.model.Movie

fun List<Movie>.ids(): List<TmdbMovieId> = map { it.tmdbId }

fun Flow<List<Movie>>.ids(): Flow<List<TmdbMovieId>> = map { movies -> movies.ids() }
