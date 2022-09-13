package cinescout.movies.domain

import arrow.core.Either
import arrow.core.NonEmptyList
import cinescout.error.DataError
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
import store.PagedStore
import store.Paging
import store.Refresh

interface MovieRepository {

    suspend fun addToDisliked(id: TmdbMovieId)

    suspend fun addToLiked(id: TmdbMovieId)

    suspend fun addToWatchlist(id: TmdbMovieId): Either<DataError.Remote, Unit>

    fun discoverMovies(params: DiscoverMoviesParams): Flow<Either<DataError, List<Movie>>>

    fun getAllDislikedMovies(): Flow<List<Movie>>

    fun getAllLikedMovies(): Flow<List<Movie>>

    fun getAllRatedMovies(refresh: Refresh): PagedStore<MovieWithPersonalRating, Paging>

    fun getAllWatchlistMovies(refresh: Refresh): PagedStore<Movie, Paging>

    fun getMovieDetails(id: TmdbMovieId, refresh: Refresh): Flow<Either<DataError, MovieWithDetails>>

    fun getMovieCredits(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Flow<Either<DataError, MovieCredits>>

    fun getMovieKeywords(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Flow<Either<DataError, MovieKeywords>>

    fun getMovieImages(
        movieId: TmdbMovieId,
        refresh: Refresh
    ): Flow<Either<DataError, MovieImages>>

    fun getRecommendationsFor(movieId: TmdbMovieId, refresh: Refresh): PagedStore<Movie, Paging>

    fun getSuggestedMovies(): Flow<Either<DataError.Local, NonEmptyList<Movie>>>

    suspend fun rate(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit>

    suspend fun removeFromWatchlist(id: TmdbMovieId): Either<DataError.Remote, Unit>

    suspend fun storeSuggestedMovies(movies: List<Movie>)

    suspend fun syncRatedMovies()
}
