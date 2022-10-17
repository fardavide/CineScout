package cinescout.movies.domain

import arrow.core.Either
import arrow.core.NonEmptyList
import cinescout.common.model.Rating
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
import kotlinx.coroutines.flow.Flow
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store

interface MovieRepository {

    suspend fun addToDisliked(movieId: TmdbMovieId)

    suspend fun addToLiked(movieId: TmdbMovieId)

    suspend fun addToWatchlist(movieId: TmdbMovieId): Either<DataError.Remote, Unit>

    fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>>

    fun getAllDislikedMovies(): Flow<List<Movie>>

    fun getAllLikedMovies(): Flow<List<Movie>>

    fun getAllRatedMovies(refresh: Refresh): PagedStore<MovieWithPersonalRating, Paging>

    fun getAllWatchlistMovies(refresh: Refresh): PagedStore<Movie, Paging>

    fun getMovieCredits(movieId: TmdbMovieId, refresh: Refresh): Store<MovieCredits>

    fun getMovieDetails(movieId: TmdbMovieId, refresh: Refresh): Store<MovieWithDetails>

    fun getMovieImages(movieId: TmdbMovieId, refresh: Refresh): Store<MovieImages>

    fun getMovieKeywords(movieId: TmdbMovieId, refresh: Refresh): Store<MovieKeywords>

    fun getMovieVideos(movieId: TmdbMovieId, refresh: Refresh): Store<MovieVideos>

    fun getRecommendationsFor(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging>

    fun getSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<Movie>>>

    suspend fun rate(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit>

    suspend fun removeFromWatchlist(id: TmdbMovieId): Either<DataError.Remote, Unit>

    fun searchMovies(query: String): PagedStore<Movie, Paging>

    suspend fun storeSuggestedMovies(movies: List<Movie>)

    suspend fun syncRatedMovies()
}
