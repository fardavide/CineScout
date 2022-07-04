package cinescout.movies.data.local

import arrow.core.Either
import arrow.core.Either.Right
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MockLocalMovieDataSource : LocalMovieDataSource {

    private val movies = MutableStateFlow(mutableSetOf<Movie>())
    private val ratings = mutableMapOf<Movie, Rating>()
    private val watchlist = mutableSetOf<Movie>()

    override fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>> =
        movies.findBy(id).map { movie ->
            movie?.let(::Right) ?: DataError.Local.NoCache.left()
        }

    override suspend fun insert(movie: Movie) {
        movies += movie
    }

    override suspend fun insertRating(movie: Movie, rating: Rating) {
        ratings += movie to rating
    }

    override suspend fun insertWatchlist(movie: Movie) {
        watchlist += movie
    }
}

private suspend operator fun MutableStateFlow<MutableSet<Movie>>.plusAssign(other: Movie) {
    val set = value.apply { add(other) }
    emit(set)
}

private fun MutableStateFlow<MutableSet<Movie>>.findBy(id: TmdbMovieId): Flow<Movie?> =
    map { set -> set.find { movie -> movie.tmdbId == id } }
