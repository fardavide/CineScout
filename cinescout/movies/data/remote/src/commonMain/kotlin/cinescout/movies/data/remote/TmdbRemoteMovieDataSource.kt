package cinescout.movies.data.remote

import arrow.core.Either
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

interface TmdbRemoteMovieDataSource {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>>

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails>

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits>

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords>

    suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages>

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

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkOperation, Unit>

    suspend fun postAddToWatchlist(id: TmdbMovieId): Either<NetworkOperation, Unit>

    suspend fun postRemoveFromWatchlist(id: TmdbMovieId): Either<NetworkOperation, Unit>

    suspend fun searchMovie(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>>
}
