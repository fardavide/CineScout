package cinescout.movies.data

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieVideos
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging

interface RemoteMovieDataSource {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>>

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails>

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits>

    suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages>

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords>

    suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos>

    suspend fun getRatedMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<MovieIdWithPersonalRating, Paging.Page.DualSources>>

    suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Paging.Page.SingleSource
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>>

    suspend fun getWatchlistMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TmdbMovieId, Paging.Page.DualSources>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun searchMovie(
        query: String,
        page: Paging.Page.SingleSource
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>>
}
