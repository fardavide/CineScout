package cinescout.movies.data

import arrow.core.Either
import arrow.core.left
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.store.PagedStore
import cinescout.store.Paging
import cinescout.store.Store
import cinescout.store.distinctUntilDataChanged
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MovieRepository {

    override suspend fun addToDisliked(id: TmdbMovieId) {
        localMovieDataSource.insertDisliked(id)
    }

    override suspend fun addToLiked(id: TmdbMovieId) {
        localMovieDataSource.insertLiked(id)
    }

    override suspend fun addToWatchlist(id: TmdbMovieId): Either<DataError.Remote, Unit> {
        localMovieDataSource.insertWatchlist(id)
        return remoteMovieDataSource.postAddToWatchlist(id).mapLeft { error ->
            DataError.Remote(error)
        }
    }

    override fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError.Remote, List<Movie>>> =
        Store(
            fetch = { remoteMovieDataSource.discoverMovies(params) },
            read = { flowOf(DataError.Local.NoCache.left()) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getAllDislikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        localMovieDataSource.findAllDislikedMovies()

    override fun getAllLikedMovies(): Flow<Either<DataError.Local, List<Movie>>> =
        localMovieDataSource.findAllLikedMovies()

    override fun getAllRatedMovies(): PagedStore<MovieWithPersonalRating, Paging.Page.DualSources> =
        PagedStore<MovieWithPersonalRating, Paging.Page.DualSources, Paging.Page.DualSources, Paging.Page.DualSources>(
            initialBookmark = Paging.Page.DualSources.Initial,
            createNextBookmark = { lastData, _ -> lastData.paging + 1 },
            fetch = { page -> remoteMovieDataSource.getRatedMovies(page) },
            read = { localMovieDataSource.findAllRatedMovies() },
            write = { localMovieDataSource.insertRatings(it) }
        ).distinctUntilDataChanged()

    override fun getMovie(id: TmdbMovieId): Flow<Either<DataError, Movie>> =
        Store(
            fetch = { remoteMovieDataSource.getMovie(id) },
            read = { localMovieDataSource.findMovie(id) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getMovieCredits(movieId: TmdbMovieId): Flow<Either<DataError.Remote, MovieCredits>> =
        Store(
            fetch = { remoteMovieDataSource.getMovieCredits(movieId) },
            read = { localMovieDataSource.findMovieCredits(movieId) },
            write = { localMovieDataSource.insertCredits(it) }
        )

    override suspend fun rate(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit> {
        localMovieDataSource.insertRating(movieId, rating)
        return remoteMovieDataSource.postRating(movieId, rating).mapLeft { networkError ->
            DataError.Remote(networkError)
        }
    }

    override suspend fun syncRatedMovies() {
        getAllRatedMovies().first()
    }
}
