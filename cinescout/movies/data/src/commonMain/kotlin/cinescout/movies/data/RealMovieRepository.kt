package cinescout.movies.data

import arrow.core.Either
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.utils.kotlin.Store
import kotlinx.coroutines.flow.Flow

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override suspend fun addToWatchlist(movie: Movie) {
        with(localMovieDataSource) {
            insert(movie)
            insertWatchlist(movie)
        }
        remoteMovieDataSource.postWatchlist(movie)
    }

    override fun getMovie(id: TmdbMovieId): Flow<Either<DataError, Movie>> =
        Store(
            fetch = { remoteMovieDataSource.getMovie(id) },
            read = { localMovieDataSource.findMovie(id) },
            write = { localMovieDataSource.insert(it) }
        )

    override suspend fun rate(movie: Movie, rating: Rating): Either<DataError, Unit> {
        with(localMovieDataSource) {
            insert(movie)
            insertRating(movie, rating)
        }
        return remoteMovieDataSource.postRating(movie, rating).mapLeft { networkError ->
            DataError.Remote(localData = DataError.Local.NoCache.left(), networkError)
        }
    }
}
