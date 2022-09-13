package cinescout.movies.data

import arrow.core.Either
import arrow.core.NonEmptyList
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner

class RealMovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val storeOwner: StoreOwner
) : MovieRepository, StoreOwner by storeOwner {

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

    override fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>> =
        Store(
            key = StoreKey("discover", params),
            fetch = { remoteMovieDataSource.discoverMovies(params) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getAllDislikedMovies(): Flow<List<Movie>> =
        localMovieDataSource.findAllDislikedMovies()

    override fun getAllLikedMovies(): Flow<List<Movie>> =
        localMovieDataSource.findAllLikedMovies()

    override fun getAllRatedMovies(refresh: Refresh): PagedStore<MovieWithPersonalRating, Paging> =
        PagedStore(
            key = StoreKey("rated_movies"),
            refresh = refresh,
            initialPage = Paging.Page.DualSources.Initial,
            fetch = { page -> remoteMovieDataSource.getRatedMovies(page) },
            read = { localMovieDataSource.findAllRatedMovies() },
            write = { localMovieDataSource.insertRatings(it) }
        )

    override fun getAllWatchlistMovies(refresh: Refresh): PagedStore<Movie, Paging> =
        PagedStore(
            key = StoreKey("watchlist"),
            refresh = refresh,
            initialPage = Paging.Page.DualSources.Initial,
            fetch = { page -> remoteMovieDataSource.getWatchlistMovies(page) },
            read = { localMovieDataSource.findAllWatchlistMovies() },
            write = { localMovieDataSource.insertWatchlist(it) },
            delete = { localMovieDataSource.deleteWatchlist(it) }
        )

    override fun getMovieDetails(id: TmdbMovieId, refresh: Refresh): Flow<Either<DataError, MovieWithDetails>> =
        Store(
            key = StoreKey("movie_details", id),
            refresh = refresh,
            fetch = { remoteMovieDataSource.getMovieDetails(id) },
            read = { localMovieDataSource.findMovieWithDetails(id) },
            write = { localMovieDataSource.insert(it) }
        )

    override fun getMovieCredits(movieId: TmdbMovieId, refresh: Refresh): Flow<Either<DataError, MovieCredits>> =
        Store(
            key = StoreKey("movie_credits", movieId),
            refresh = refresh,
            fetch = { remoteMovieDataSource.getMovieCredits(movieId) },
            read = { localMovieDataSource.findMovieCredits(movieId) },
            write = { localMovieDataSource.insertCredits(it) }
        )

    override fun getMovieKeywords(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Flow<Either<DataError, MovieKeywords>> = Store(
        key = StoreKey("movie_keywords", movieId),
        refresh = refresh,
        fetch = { remoteMovieDataSource.getMovieKeywords(movieId) },
        read = { localMovieDataSource.findMovieKeywords(movieId) },
        write = { localMovieDataSource.insertKeywords(it) }
    )

    override fun getMovieImages(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Flow<Either<DataError, MovieImages>> = Store(
        key = StoreKey("movie_images", movieId),
        refresh = refresh,
        fetch = { remoteMovieDataSource.getMovieImages(movieId) },
        read = { localMovieDataSource.findMovieImages(movieId) },
        write = { localMovieDataSource.insertImages(it) }
    )

    override fun getRecommendationsFor(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging> =
        PagedStore(
            key = StoreKey("recommendations", movieId),
            refresh = refresh,
            initialPage = Paging.Page.SingleSource.Initial,
            fetch = { page -> remoteMovieDataSource.getRecommendationsFor(movieId, page) },
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

    override suspend fun storeSuggestedMovies(movies: List<Movie>) {
        localMovieDataSource.insertSuggestedMovies(movies)
    }

    override suspend fun syncRatedMovies() {
        getAllRatedMovies(Refresh.Once).first()
    }
}
