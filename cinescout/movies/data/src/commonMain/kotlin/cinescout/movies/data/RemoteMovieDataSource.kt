package cinescout.movies.data

import arrow.core.Either
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

    suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<Movie>>

    suspend fun getWatchlistMovies(): Either<NetworkOperation, List<TmdbMovieId>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun searchMovie(query: String, page: Paging.Page): Either<NetworkError, PagedData.Remote<Movie>>
}
