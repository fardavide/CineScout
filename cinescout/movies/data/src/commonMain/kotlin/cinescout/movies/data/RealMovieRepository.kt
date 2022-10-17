package cinescout.movies.data

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.continuations.either
import cinescout.common.model.Rating
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import store.Fetcher
import store.PagedFetcher
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner
import store.ext.requireFirst

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

    override fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>> =
        Store(
            key = StoreKey("discover", params),
            fetch = Fetcher.forError { remoteMovieDataSource.discoverMovies(params) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getAllDislikedMovies(): Flow<List<Movie>> =
        localMovieDataSource.findAllDislikedMovies()

    override fun getAllLikedMovies(): Flow<List<Movie>> =
        localMovieDataSource.findAllLikedMovies()

    override fun getAllRatedMovies(refresh: Refresh): PagedStore<MovieWithPersonalRating, Paging> =
        PagedStore(
            key = StoreKey<Movie>("rated"),
            refresh = refresh,
            initialPage = Paging.Page.DualSources.Initial,
            fetch = PagedFetcher.forOperation { page ->
                either {
                    val ratedIds = remoteMovieDataSource.getRatedMovies(page).bind()
                    ratedIds.map { (movieId, personalRating) ->
                        val details = getMovieDetails(movieId, refresh).requireFirst()
                            .mapLeft(NetworkOperation::Error)
                            .bind()
                        MovieWithPersonalRating(details.movie, personalRating)
                    }
                }
            },
            read = { localMovieDataSource.findAllRatedMovies() },
            write = { localMovieDataSource.insertRatings(it) }
        )

    override fun getAllWatchlistMovies(refresh: Refresh): PagedStore<Movie, Paging> =
        PagedStore(
            key = StoreKey<Movie>("watchlist"),
            refresh = refresh,
            initialPage = Paging.Page.DualSources.Initial,
            fetch = PagedFetcher.forOperation { page ->
                either {
                    val watchlistIds = remoteMovieDataSource.getWatchlistMovies(page).bind()
                    val watchlistWithDetails = watchlistIds.map { id ->
                        getMovieDetails(id, refresh).requireFirst()
                            .mapLeft(NetworkOperation::Error)
                            .bind()
                    }
                    watchlistWithDetails.map { it.movie }
                }
            },
            read = { localMovieDataSource.findAllWatchlistMovies() },
            write = { localMovieDataSource.insertWatchlist(it) },
            delete = { localMovieDataSource.deleteWatchlist(it) }
        )

    override fun getMovieDetails(movieId: TmdbMovieId, refresh: Refresh): Store<MovieWithDetails> =
        Store(
            key = StoreKey("movie_details", movieId),
            refresh = refresh,
            fetch = Fetcher.forError { remoteMovieDataSource.getMovieDetails(movieId) },
            read = { localMovieDataSource.findMovieWithDetails(movieId) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getMovieCredits(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Store<MovieCredits> =
        Store(
            key = StoreKey("movie_credits", movieId),
            refresh = refresh,
            fetch = Fetcher.forError { remoteMovieDataSource.getMovieCredits(movieId) },
            read = { localMovieDataSource.findMovieCredits(movieId) },
            write = { localMovieDataSource.insertCredits(it) }
        )

    override fun getMovieKeywords(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Store<MovieKeywords> = Store(
        key = StoreKey("movie_keywords", movieId),
        refresh = refresh,
        fetch = Fetcher.forError { remoteMovieDataSource.getMovieKeywords(movieId) },
        read = { localMovieDataSource.findMovieKeywords(movieId) },
        write = { localMovieDataSource.insertKeywords(it) }
    )

    override fun getMovieImages(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Store<MovieImages> = Store(
        key = StoreKey("movie_images", movieId),
        refresh = refresh,
        fetch = Fetcher.forError { remoteMovieDataSource.getMovieImages(movieId) },
        read = { localMovieDataSource.findMovieImages(movieId) },
        write = { localMovieDataSource.insertImages(it) }
    )

    override fun getMovieVideos(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Store<MovieVideos> = Store(
        key = StoreKey("movie_videos", movieId),
        refresh = refresh,
        fetch = Fetcher.forError { remoteMovieDataSource.getMovieVideos(movieId) },
        read = { localMovieDataSource.findMovieVideos(movieId) },
        write = { localMovieDataSource.insertVideos(it) }
    )

    override fun getRecommendationsFor(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging> =
        PagedStore(
            key = StoreKey("recommendations", movieId),
            refresh = refresh,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = PagedFetcher.forError { page -> remoteMovieDataSource.getRecommendationsFor(movieId, page) },
            read = { localMovieDataSource.findRecommendationsFor(movieId) },
            write = { recommendedMovies ->
                localMovieDataSource.insertRecommendations(movieId = movieId, recommendations = recommendedMovies)
            }
        )

    override fun getSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<Movie>>> =
        localMovieDataSource.findAllSuggestedMovies().distinctUntilChanged()

    override suspend fun rate(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit> {
        localMovieDataSource.insertRating(movieId, rating)
        return remoteMovieDataSource.postRating(movieId, rating).mapLeft { networkError ->
            DataError.Remote(networkError)
        }
    }

    override suspend fun removeFromWatchlist(id: TmdbMovieId): Either<DataError.Remote, Unit> {
        localMovieDataSource.deleteWatchlist(id)
        return remoteMovieDataSource.postRemoveFromWatchlist(id).mapLeft { error ->
            DataError.Remote(error)
        }
    }

    override fun searchMovies(query: String): PagedStore<Movie, Paging> =
        PagedStore(
            key = StoreKey("search", query),
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = PagedFetcher.forError { page -> remoteMovieDataSource.searchMovie(query, page) },
            read = { localMovieDataSource.findMoviesByQuery(query) },
            write = { movies -> localMovieDataSource.insert(movies) }
        )

    override suspend fun storeSuggestedMovies(movies: List<Movie>) {
        localMovieDataSource.insertSuggestedMovies(movies)
    }

    override suspend fun syncRatedMovies() {
        getAllRatedMovies(Refresh.Once).first()
    }
}
