package cinescout.movies.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Rating
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.Fetcher
import store.PagedFetcher
import store.PagedReader
import store.PagedStore
import store.Paging
import store.Reader
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner

@Factory
internal class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    storeOwner: StoreOwner
) : MovieRepository, StoreOwner by storeOwner {

    override suspend fun addToDisliked(movieId: TmdbMovieId) {
        localMovieDataSource.insertDisliked(movieId)
    }

    override suspend fun addToLiked(movieId: TmdbMovieId) {
        localMovieDataSource.insertLiked(movieId)
    }

    override suspend fun addToWatchlist(movieId: TmdbMovieId): Either<DataError.Remote, Unit> {
        localMovieDataSource.insertWatchlist(movieId)
        return remoteMovieDataSource.postAddToWatchlist(movieId).mapLeft { error ->
            DataError.Remote(error)
        }
    }

    override fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>> = Store(
        key = StoreKey("discover", params),
        fetcher = Fetcher.forError { remoteMovieDataSource.discoverMovies(params) },
        write = { localMovieDataSource.insert(it) }
    )

    override fun getAllDislikedMovies(): Flow<List<Movie>> = localMovieDataSource.findAllDislikedMovies()

    override fun getAllLikedMovies(): Flow<List<Movie>> = localMovieDataSource.findAllLikedMovies()

    override fun getMovieImages(movieId: TmdbMovieId, refresh: Refresh): Store<MovieImages> = Store(
        key = StoreKey<MovieImages>(movieId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteMovieDataSource.getMovieImages(movieId) },
        reader = Reader.fromSource { localMovieDataSource.findMovieImages(movieId) },
        write = { localMovieDataSource.insertImages(it) }
    )

    override fun getMovieVideos(movieId: TmdbMovieId, refresh: Refresh): Store<MovieVideos> = Store(
        key = StoreKey<MovieVideos>(movieId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteMovieDataSource.getMovieVideos(movieId) },
        reader = Reader.fromSource { localMovieDataSource.findMovieVideos(movieId) },
        write = { localMovieDataSource.insertVideos(it) }
    )

    override fun getSimilarMovies(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging> =
        PagedStore(
            key = StoreKey("similar", movieId),
            refresh = refresh,
            fetcher = PagedFetcher.forError { page -> remoteMovieDataSource.getSimilarMovies(movieId, page) },
            reader = PagedReader.fromSource { localMovieDataSource.findSimilarMovies(movieId) },
            write = { similarMovies ->
                localMovieDataSource.insertSimilarMovies(movieId = movieId, similarMovies = similarMovies)
            }
        )

    override suspend fun rate(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit> {
        localMovieDataSource.insertRating(movieId, rating)
        return remoteMovieDataSource.postRating(movieId, rating).mapLeft { networkError ->
            DataError.Remote(networkError)
        }
    }

    override suspend fun removeFromWatchlist(movieId: TmdbMovieId): Either<DataError.Remote, Unit> {
        localMovieDataSource.deleteWatchlist(movieId)
        return remoteMovieDataSource.postRemoveFromWatchlist(movieId).mapLeft { error ->
            DataError.Remote(error)
        }
    }

    override fun searchMovies(query: String): PagedStore<Movie, Paging> = PagedStore(
        key = StoreKey("search_movie", query),
        fetcher = PagedFetcher.forError { page -> remoteMovieDataSource.searchMovie(query, page) },
        reader = PagedReader.fromSource { localMovieDataSource.findMoviesByQuery(query) },
        write = { movies -> localMovieDataSource.insert(movies) }
    )

}
