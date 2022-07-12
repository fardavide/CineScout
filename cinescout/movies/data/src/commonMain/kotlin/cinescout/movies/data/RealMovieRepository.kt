package cinescout.movies.data

import arrow.core.Either
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.utils.kotlin.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override suspend fun addToWatchlist(movie: Movie) {
        localMovieDataSource.insertWatchlist(movie)
        remoteMovieDataSource.postWatchlist(movie)
    }

    override fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>> =
        Store(
            fetch = { remoteMovieDataSource.discoverMovies(params) },
            read = { flowOf(DataError.Local.NoCache.left()) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getAllRatedMovies(): Flow<Either<DataError, List<MovieWithRating>>> =
        Store(
            fetch = { remoteMovieDataSource.getRatedMovies() },
            read = { localMovieDataSource.findAllRatedMovies() },
            write = { localMovieDataSource.insertRatings(it) }
        )

    override fun getMovie(id: TmdbMovieId): Flow<Either<DataError, Movie>> =
        Store(
            fetch = { remoteMovieDataSource.getMovie(id) },
            read = { localMovieDataSource.findMovie(id) },
            write = { localMovieDataSource.insert(it) }
        )

    override suspend fun rate(movie: Movie, rating: Rating): Either<DataError, Unit> {
        localMovieDataSource.insertRating(movie, rating)
        return remoteMovieDataSource.postRating(movie, rating).mapLeft { networkError ->
            DataError.Remote(localData = DataError.Local.NoCache.left(), networkError)
        }
    }
}
