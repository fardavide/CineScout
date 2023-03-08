package cinescout.movies.data.remote

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.data.RemoteMovieDataSource
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
import org.koin.core.annotation.Factory
import store.PagedData
import store.Paging

@Factory
class RealRemoteMovieDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val tmdbSource: TmdbRemoteMovieDataSource,
    private val traktSource: TraktRemoteMovieDataSource
) : RemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        tmdbSource.discoverMovies(params)

    override suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails> =
        tmdbSource.getMovieDetails(id)

    override suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits> =
        tmdbSource.getMovieCredits(movieId)

    override suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages> =
        tmdbSource.getMovieImages(movieId)

    override suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords> =
        tmdbSource.getMovieKeywords(movieId)

    override suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos> =
        tmdbSource.getMovieVideos(movieId)

    override suspend fun getRatedMovies(): Either<NetworkOperation, List<MovieIdWithPersonalRating>> =
        callWithTraktAccount {
            traktSource.getRatedMovies().map { list ->
                list.map { traktPersonalMovieRating ->
                    MovieIdWithPersonalRating(
                        traktPersonalMovieRating.tmdbId,
                        traktPersonalMovieRating.rating
                    )
                }
            }
        }

    override suspend fun getSimilarMovies(
        movieId: TmdbMovieId,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<Movie>> = tmdbSource.getRecommendationsFor(movieId, page.page)

    override suspend fun getWatchlistMovies(): Either<NetworkOperation, List<TmdbMovieId>> =
        callWithTraktAccount {
            traktSource.getWatchlistMovies()
        }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        callWithTraktAccount.forUnit { traktSource.postRating(movieId, rating) }

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> =
        callWithTraktAccount.forUnit { traktSource.postAddToWatchlist(movieId) }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> =
        callWithTraktAccount.forUnit { traktSource.postRemoveFromWatchlist(movieId) }

    override suspend fun searchMovie(
        query: String,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<Movie>> = tmdbSource.searchMovie(query = query, page = page.page)
}
