package cinescout.movies.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow

interface LocalMovieDataSource {

    fun findAllRatedMovies(): Flow<Either<DataError.Local, List<MovieWithRating>>>

    fun findMovie(id: TmdbMovieId): Flow<Either<DataError.Local, Movie>>

    suspend fun insert(movie: Movie)

    suspend fun insertRating(movie: Movie, rating: Rating)

    suspend fun insertRatings(moviesWithRating: Collection<MovieWithRating>)

    suspend fun insertWatchlist(movie: Movie)
}
