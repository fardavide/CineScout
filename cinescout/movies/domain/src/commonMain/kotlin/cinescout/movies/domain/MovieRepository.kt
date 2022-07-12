package cinescout.movies.domain

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun addToWatchlist(movie: Movie)

    fun getAllRatedMovies(): Flow<Either<DataError, List<MovieWithRating>>>

    fun getMovie(id: TmdbMovieId): Flow<Either<DataError, Movie>>

    suspend fun rate(movie: Movie, rating: Rating): Either<DataError, Unit>
}
