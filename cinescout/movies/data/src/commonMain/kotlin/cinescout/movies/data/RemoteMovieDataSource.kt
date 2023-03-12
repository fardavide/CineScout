package cinescout.movies.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.MovieImages
import cinescout.screenplay.domain.model.Rating
import store.PagedData
import store.Paging

interface RemoteMovieDataSource {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>>

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails>

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits>

    suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages>

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords>

    suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos>

    suspend fun getRatedMovies(): Either<NetworkOperation, List<MovieIdWithPersonalRating>>

    suspend fun getSimilarMovies(
        movieId: TmdbMovieId,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<Movie>>

    suspend fun getWatchlistMovies(): Either<NetworkOperation, List<TmdbMovieId>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>
    suspend fun searchMovie(query: String, page: Paging.Page): Either<NetworkError, PagedData.Remote<Movie>>
}

class FakeRemoteMovieDataSource(
    private val discoverMovies: List<Movie>? = null,
    private val moviesDetails: List<MovieWithDetails>? = null,
    private val ratedMovieIds: List<MovieIdWithPersonalRating>? = null,
    private val watchlistMovieIds: List<TmdbMovieId>? = null
) : RemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        discoverMovies?.right() ?: NetworkError.Unknown.left()

    override suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails> =
        moviesDetails?.find { it.movie.tmdbId == id }?.right() ?: NetworkError.NotFound.left()

    override suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos> {
        TODO("Not yet implemented")
    }

    override suspend fun getRatedMovies(): Either<NetworkOperation, List<MovieIdWithPersonalRating>> =
        ratedMovieIds?.right() ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun getSimilarMovies(
        movieId: TmdbMovieId,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchlistMovies(): Either<NetworkOperation, List<TmdbMovieId>> =
        watchlistMovieIds?.right() ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMovie(
        query: String,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<Movie>> {
        TODO("Not yet implemented")
    }
}
