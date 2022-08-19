package cinescout.movies.data

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import store.PagedData
import store.Paging

interface RemoteMovieDataSource {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>>

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails>

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits>

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords>

    suspend fun getRatedMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<MovieWithPersonalRating, Paging.Page.DualSources>>

    suspend fun getWatchlistMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.DualSources>>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(id: TmdbMovieId): Either<NetworkError, Unit>
}
