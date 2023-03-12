package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.MovieImages
import store.PagedData
import store.Paging
import store.builder.remotePagedDataOf

interface TmdbRemoteMovieDataSource {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>>

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits>

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails>

    suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages>

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords>

    suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos>

    suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie>>

    suspend fun searchMovie(query: String, page: Int): Either<NetworkError, PagedData.Remote<Movie>>
}

class FakeTmdbRemoteMovieDataSource(
    private val discoverMovies: List<Movie>? = null,
    private val movieCredits: MovieCredits? = null,
    private val movieDetails: MovieWithDetails? = null,
    private val movieImages: MovieImages? = null,
    private val movieKeywords: MovieKeywords? = null,
    private val movieVideos: MovieVideos? = null,
    private val recommendedMovies: List<Movie>? = null,
    private val searchMovies: List<Movie>? = null
) : TmdbRemoteMovieDataSource {

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

    override suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie>> = recommendedMovies
        ?.let { movies -> remotePagedDataOf(*movies.toTypedArray(), paging = Paging.Page.Initial).right() }
        ?: NetworkError.Unknown.left()

    override suspend fun searchMovie(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie>> = searchMovies
        ?.let { movies -> remotePagedDataOf(*movies.toTypedArray(), paging = Paging.Page.Initial).right() }
        ?: NetworkError.Unknown.left()
}
