package cinescout.movies.data.remote

import arrow.core.Either
import cinescout.common.model.Rating
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
import cinescout.network.dualSourceCall
import cinescout.network.dualSourceCallWithResult
import store.PagedData
import store.Paging

class RealRemoteMovieDataSource(
    private val tmdbSource: TmdbRemoteMovieDataSource,
    private val traktSource: TraktRemoteMovieDataSource
) : RemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        tmdbSource.discoverMovies(params)

    override suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, MovieWithDetails> =
        tmdbSource.getMovieDetails(id)

    override suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits> =
        tmdbSource.getMovieCredits(movieId)

    override suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, MovieKeywords> =
        tmdbSource.getMovieKeywords(movieId)

    override suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, MovieImages> =
        tmdbSource.getMovieImages(movieId)

    override suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, MovieVideos> =
        tmdbSource.getMovieVideos(movieId)

    override suspend fun getRatedMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<MovieIdWithPersonalRating, Paging.Page.DualSources>> =
        dualSourceCallWithResult(
            page = page,
            firstSourceCall = { paging ->
                tmdbSource.getRatedMovies(paging.page).map { data ->
                    data.map { movieWithPersonalRating ->
                        MovieIdWithPersonalRating(
                            movieWithPersonalRating.movie.tmdbId,
                            movieWithPersonalRating.personalRating
                        )
                    }
                }
            },
            secondSourceCall = { paging ->
                traktSource.getRatedMovies(paging.page).map { data ->
                    data.map { traktPersonalMovieRating ->
                        MovieIdWithPersonalRating(
                            traktPersonalMovieRating.tmdbId,
                            traktPersonalMovieRating.rating
                        )
                    }
                }
            },
            id = { movieIdWithPersonalRating -> movieIdWithPersonalRating.movieId }
        )

    override suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Paging.Page.SingleSource
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> =
        tmdbSource.getRecommendationsFor(movieId, page.page)

    override suspend fun getWatchlistMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TmdbMovieId, Paging.Page.DualSources>> =
        dualSourceCallWithResult(
            page = page,
            firstSourceCall = { paging ->
                tmdbSource.getWatchlistMovies(paging.page).map { data ->
                    data.map { movie -> movie.tmdbId }
                }
            },
            secondSourceCall = { traktSource.getWatchlistMovies(it.page) }
        )

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postRating(movieId, rating) },
            secondSourceCall = { traktSource.postRating(movieId, rating) }
        )

    override suspend fun postAddToWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postAddToWatchlist(id) },
            secondSourceCall = { traktSource.postAddToWatchlist(id) }
        )

    override suspend fun postRemoveFromWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postRemoveFromWatchlist(id) },
            secondSourceCall = { traktSource.postRemoveFromWatchlist(id) }
        )

    override suspend fun searchMovie(
        query: String,
        page: Paging.Page.SingleSource
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> =
        tmdbSource.searchMovie(query = query, page = page.page)
}
