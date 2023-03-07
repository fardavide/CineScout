package cinescout.movies.data

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.model.NetworkOperation
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import store.Fetcher
import store.PagedFetcher
import store.PagedStore
import store.Paging
import store.Reader
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner
import store.ext.requireFirst

@Factory
class RealMovieRepository(
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

    override fun getAllRatedMovies(refresh: Refresh): Store<List<MovieWithPersonalRating>> = Store(
        key = StoreKey<Movie>("rated"),
        refresh = refresh,
        fetcher = Fetcher.buildForOperation {
            either {
                val ratedIds = remoteMovieDataSource.getRatedMovies()
                    .bind()

                ratedIds.fold(emptyList<MovieWithPersonalRating>()) { acc, (movieId, personalRating) ->
                    val details = getMovieDetails(movieId, Refresh.IfNeeded).requireFirst()
                        .mapLeft(NetworkOperation::Error)
                        .bind()
                    val movieWithPersonalRating = MovieWithPersonalRating(details.movie, personalRating)

                    (acc + movieWithPersonalRating).also { list -> emit(list.right()) }
                }
            }.onLeft { networkOperation -> emit(networkOperation.left()) }
        },
        reader = Reader.fromSource(localMovieDataSource.findAllRatedMovies()),
        write = { localMovieDataSource.insertRatings(it) }
    )

    override fun getAllWatchlistMovies(refresh: Refresh): Store<List<Movie>> = Store(
        key = StoreKey<Movie>("watchlist"),
        refresh = refresh,
        fetcher = Fetcher.buildForOperation {
            either {
                val watchlistIds = remoteMovieDataSource.getWatchlistMovies()
                    .bind()

                localMovieDataSource.deleteWatchlistExcept(watchlistIds)

                watchlistIds.fold(emptyList<Movie>()) { acc, movieId ->
                    val details = getMovieDetails(movieId, Refresh.IfNeeded).requireFirst()
                        .mapLeft(NetworkOperation::Error)
                        .bind()
                    val movie = details.movie

                    (acc + movie).also { list -> emit(list.right()) }
                }
            }.onLeft { networkOperation -> emit(networkOperation.left()) }
        },
        reader = Reader.fromSource(localMovieDataSource.findAllWatchlistMovies()),
        write = { localMovieDataSource.insertWatchlist(it) }
    )

    override fun getMovieDetails(movieId: TmdbMovieId, refresh: Refresh): Store<MovieWithDetails> = Store(
        key = StoreKey("movie_details", movieId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteMovieDataSource.getMovieDetails(movieId) },
        reader = Reader.fromSource { localMovieDataSource.findMovieWithDetails(movieId) },
        write = { localMovieDataSource.insert(it) }
    )

    override fun getMovieCredits(movieId: TmdbMovieId, refresh: Refresh): Store<MovieCredits> = Store(
        key = StoreKey("movie_credits", movieId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteMovieDataSource.getMovieCredits(movieId) },
        reader = Reader.fromSource { localMovieDataSource.findMovieCredits(movieId) },
        write = { localMovieDataSource.insertCredits(it) }
    )

    override fun getMovieImages(movieId: TmdbMovieId, refresh: Refresh): Store<MovieImages> = Store(
        key = StoreKey("movie_images", movieId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteMovieDataSource.getMovieImages(movieId) },
        reader = Reader.fromSource { localMovieDataSource.findMovieImages(movieId) },
        write = { localMovieDataSource.insertImages(it) }
    )

    override fun getMovieKeywords(movieId: TmdbMovieId, refresh: Refresh): Store<MovieKeywords> = Store(
        key = StoreKey("movie_keywords", movieId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteMovieDataSource.getMovieKeywords(movieId) },
        reader = Reader.fromSource { localMovieDataSource.findMovieKeywords(movieId) },
        write = { localMovieDataSource.insertKeywords(it) }
    )

    override fun getMovieVideos(movieId: TmdbMovieId, refresh: Refresh): Store<MovieVideos> = Store(
        key = StoreKey("movie_videos", movieId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteMovieDataSource.getMovieVideos(movieId) },
        reader = Reader.fromSource { localMovieDataSource.findMovieVideos(movieId) },
        write = { localMovieDataSource.insertVideos(it) }
    )

    override fun getRecommendationsFor(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging> =
        PagedStore(
            key = StoreKey("recommendations", movieId),
            refresh = refresh,
            initialPage = Paging.Page.Initial,
            fetch = PagedFetcher.forError { page -> remoteMovieDataSource.getRecommendationsFor(movieId, page) },
            read = { localMovieDataSource.findRecommendationsFor(movieId) },
            write = { recommendedMovies ->
                localMovieDataSource.insertRecommendations(movieId = movieId, recommendations = recommendedMovies)
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
        initialPage = Paging.Page.Initial,
        fetch = PagedFetcher.forError { page -> remoteMovieDataSource.searchMovie(query, page) },
        read = { localMovieDataSource.findMoviesByQuery(query) },
        write = { movies -> localMovieDataSource.insert(movies) }
    )

    override suspend fun syncRatedMovies() {
        getAllRatedMovies(Refresh.Once).first()
    }
}
