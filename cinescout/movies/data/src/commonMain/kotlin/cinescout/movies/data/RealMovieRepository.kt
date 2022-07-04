package cinescout.movies.data

import arrow.core.Either
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override suspend fun getMovie(id: TmdbMovieId): Either<DataError, Movie> =
        remoteMovieDataSource.getMovie(id).mapLeft { networkError ->
            DataError.Remote(
                localData = DataError.Local.NoCache.left(),
                networkError = networkError
            )
        }

    override suspend fun addToWatchlist(movie: Movie) {
        with(localMovieDataSource) {
            insert(movie)
            insertWatchlist(movie)
        }
        remoteMovieDataSource.postWatchlist(movie)
    }

    override suspend fun rate(movie: Movie, rating: Rating) {
        with(localMovieDataSource) {
            insert(movie)
            insertRating(movie, rating)
        }
        remoteMovieDataSource.postRating(movie, rating)
    }
}
