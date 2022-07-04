package cinescout.movies.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow

interface LocalMovieDataSource {

    fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>>

    suspend fun insert(movie: Movie)

    suspend fun insertRating(movie: Movie, rating: Rating)

    suspend fun insertWatchlist(movie: Movie)
}
