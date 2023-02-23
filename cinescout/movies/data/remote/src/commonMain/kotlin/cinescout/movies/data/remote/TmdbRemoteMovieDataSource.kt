package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging
import store.builder.pagedDataOf

interface TmdbRemoteMovieDataSource {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>>

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits>

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails>

    suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages>

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords>

    suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos>

    suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<MovieWithPersonalRating, Paging.Page.SingleSource>>

    suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>>

    suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<Movie, Paging.Page.SingleSource>>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkOperation, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit>

    suspend fun searchMovie(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>>
}

class FakeTmdbRemoteMovieDataSource(
    private val discoverMovies: List<Movie>? = null,
    private val movieCredits: MovieCredits? = null,
    private val movieDetails: MovieWithDetails? = null,
    private val movieImages: MovieImages? = null,
    private val movieKeywords: MovieKeywords? = null,
    private val movieVideos: MovieVideos? = null,
    private val ratedMovies: List<MovieWithPersonalRating>? = null,
    private val recommendedMovies: List<Movie>? = null,
    private val watchlistMovies: List<Movie>? = null,
    private val searchMovies: List<Movie>? = null
) : TmdbRemoteMovieDataSource {

    var postAddToWatchlistInvoked: Boolean = false
        private set
    var postRatingInvoked: Boolean = false
        private set
    var postRemoveFromWatchlistInvoked: Boolean = false
        private set

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        discoverMovies?.right() ?: NetworkError.Unknown.left()

    override suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits> =
        movieCredits?.right() ?: NetworkError.Unknown.left()

    override suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails> =
        movieDetails?.right() ?: NetworkError.Unknown.left()

    override suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages> =
        movieImages?.right() ?: NetworkError.Unknown.left()

    override suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords> =
        movieKeywords?.right() ?: NetworkError.Unknown.left()

    override suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos> =
        movieVideos?.right() ?: NetworkError.Unknown.left()

    override suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<MovieWithPersonalRating, Paging.Page.SingleSource>> =
        ratedMovies
            ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.SingleSource.Initial).right() }
            ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> = recommendedMovies
        ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.SingleSource.Initial).right() }
        ?: NetworkError.Unknown.left()

    override suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<Movie, Paging.Page.SingleSource>> = watchlistMovies
        ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.SingleSource.Initial).right() }
        ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit> {
        postAddToWatchlistInvoked = true
        return Unit.right()
    }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkOperation, Unit> {
        postRatingInvoked = true
        return Unit.right()
    }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkOperation, Unit> {
        postRemoveFromWatchlistInvoked = true
        return Unit.right()
    }

    override suspend fun searchMovie(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> = searchMovies
        ?.let { movies -> pagedDataOf(*movies.toTypedArray(), paging = Paging.Page.SingleSource.Initial).right() }
        ?: NetworkError.Unknown.left()
}
