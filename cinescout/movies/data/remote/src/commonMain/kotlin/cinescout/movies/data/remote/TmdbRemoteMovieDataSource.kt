package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Movie
import store.PagedData
import store.Paging
import store.builder.remotePagedDataOf

interface TmdbRemoteMovieDataSource {

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails>

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords>

    suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie>>

    suspend fun searchMovie(query: String, page: Int): Either<NetworkError, PagedData.Remote<Movie>>
}

class FakeTmdbRemoteMovieDataSource(
    private val movieDetails: MovieWithDetails? = null,
    private val movieKeywords: MovieKeywords? = null,
    private val recommendedMovies: List<Movie>? = null,
    private val searchMovies: List<Movie>? = null
) : TmdbRemoteMovieDataSource {

    override suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails> =
        movieDetails?.right() ?: NetworkError.Unknown.left()

    override suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords> =
        movieKeywords?.right() ?: NetworkError.Unknown.left()

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
