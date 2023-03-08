package cinescout.movies.domain

import arrow.core.Either
import cinescout.error.DataError
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
import kotlinx.coroutines.flow.flowOf
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store
import store.builder.storeOf

interface MovieRepository {

    suspend fun addToDisliked(movieId: TmdbMovieId)

    suspend fun addToLiked(movieId: TmdbMovieId)

    suspend fun addToWatchlist(movieId: TmdbMovieId): Either<DataError.Remote, Unit>

    fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>>

    fun getAllDislikedMovies(): Flow<List<Movie>>

    fun getAllLikedMovies(): Flow<List<Movie>>

    fun getAllRatedMovies(refresh: Refresh): Store<List<MovieWithPersonalRating>>

    fun getAllWatchlistMovies(refresh: Refresh): Store<List<Movie>>

    fun getMovieCredits(movieId: TmdbMovieId, refresh: Refresh): Store<MovieCredits>

    fun getMovieDetails(movieId: TmdbMovieId, refresh: Refresh): Store<MovieWithDetails>

    fun getMovieImages(movieId: TmdbMovieId, refresh: Refresh): Store<MovieImages>

    fun getMovieKeywords(movieId: TmdbMovieId, refresh: Refresh): Store<MovieKeywords>

    fun getMovieVideos(movieId: TmdbMovieId, refresh: Refresh): Store<MovieVideos>

    fun getSimilarMovies(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging>

    suspend fun rate(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit>

    suspend fun removeFromWatchlist(movieId: TmdbMovieId): Either<DataError.Remote, Unit>

    fun searchMovies(query: String): PagedStore<Movie, Paging>

    suspend fun syncRatedMovies()
}

class FakeMovieRepository(
    private val dislikedMovies: List<Movie> = emptyList(),
    private val likedMovies: List<Movie> = emptyList(),
    private val ratedMovies: List<MovieWithPersonalRating> = emptyList(),
    private val watchlistMovies: List<Movie> = emptyList()
) : MovieRepository {

    override suspend fun addToDisliked(movieId: TmdbMovieId) {
        TODO("Not yet implemented")
    }

    override suspend fun addToLiked(movieId: TmdbMovieId) {
        TODO("Not yet implemented")
    }

    override suspend fun addToWatchlist(movieId: TmdbMovieId): Either<DataError.Remote, Unit> {
        TODO("Not yet implemented")
    }

    override fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun getAllDislikedMovies(): Flow<List<Movie>> = flowOf(dislikedMovies)

    override fun getAllLikedMovies(): Flow<List<Movie>> = flowOf(likedMovies)

    override fun getAllRatedMovies(refresh: Refresh): Store<List<MovieWithPersonalRating>> =
        storeOf(ratedMovies)

    override fun getAllWatchlistMovies(refresh: Refresh): Store<List<Movie>> = storeOf(watchlistMovies)

    override fun getMovieCredits(movieId: TmdbMovieId, refresh: Refresh): Store<MovieCredits> {
        TODO("Not yet implemented")
    }

    override fun getMovieDetails(movieId: TmdbMovieId, refresh: Refresh): Store<MovieWithDetails> {
        TODO("Not yet implemented")
    }

    override fun getMovieImages(movieId: TmdbMovieId, refresh: Refresh): Store<MovieImages> {
        TODO("Not yet implemented")
    }

    override fun getMovieKeywords(movieId: TmdbMovieId, refresh: Refresh): Store<MovieKeywords> {
        TODO("Not yet implemented")
    }

    override fun getMovieVideos(movieId: TmdbMovieId, refresh: Refresh): Store<MovieVideos> {
        TODO("Not yet implemented")
    }

    override fun getSimilarMovies(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging> {
        TODO("Not yet implemented")
    }

    override suspend fun rate(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromWatchlist(movieId: TmdbMovieId): Either<DataError.Remote, Unit> {
        TODO("Not yet implemented")
    }

    override fun searchMovies(query: String): PagedStore<Movie, Paging> {
        TODO("Not yet implemented")
    }

    override suspend fun syncRatedMovies() {
        TODO("Not yet implemented")
    }
}
