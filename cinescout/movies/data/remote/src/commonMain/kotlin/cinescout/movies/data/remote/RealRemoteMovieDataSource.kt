package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import cinescout.error.NetworkError
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.MovieWithDetails
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.getOrThrow
import cinescout.network.DualSourceCall
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.first
import store.PagedData
import store.Paging
import store.builder.mergePagedData

class RealRemoteMovieDataSource(
    private val dualSourceCall: DualSourceCall,
    private val isTmdbLinked: IsTmdbLinked,
    private val isTraktLinked: IsTraktLinked,
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


    override suspend fun getRatedMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<MovieWithPersonalRating, Paging.Page.DualSources>> =
        either {
            val isTmdbLinked = isTmdbLinked().first()
            val isTraktLinked = isTraktLinked().first()

            val fromTmdb = if (isTmdbLinked && page.first.isValid()) {
                tmdbSource.getRatedMovies(page.first.page).bind()
            } else {
                PagedData.Remote(emptyList(), page.first)
            }
            val fromTrakt = if (isTraktLinked && page.second.isValid()) {
                val ratingWithIds = traktSource.getRatedMovies(page.second.page).bind()
                ratingWithIds.map {
                    MovieWithPersonalRating(
                        movie = getMovieDetails(it.tmdbId).bind().movie,
                        personalRating = it.rating
                    )
                }
            } else {
                PagedData.Remote(emptyList(), page.second)
            }
            mergePagedData(
                first = fromTmdb,
                second = fromTrakt,
                id = { movieWithRating -> movieWithRating.movie.tmdbId },
                onConflict = { first, second ->
                    val averageRating = run {
                        val double = (first.personalRating.value + second.personalRating.value) / 2
                        Rating.of(double).getOrThrow()
                    }
                    first.copy(personalRating = averageRating)
                }
            )
        }

    override suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Paging.Page.SingleSource
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.SingleSource>> =
        tmdbSource.getRecommendationsFor(movieId, page.page)

    override suspend fun getWatchlistMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<Movie, Paging.Page.DualSources>> =
        either {
            val isTmdbLinked = isTmdbLinked().first()
            val isTraktLinked = isTraktLinked().first()

            val fromTmdb = if (isTmdbLinked && page.first.isValid()) {
                Logger.v("Fetching Tmdb watchlist: ${page.first}")
                tmdbSource.getWatchlistMovies(page.first.page).bind()
            } else {
                PagedData.Remote(emptyList(), page.first)
            }
            val fromTrakt = if (isTraktLinked && page.second.isValid()) {
                Logger.v("Fetching Trakt watchlist: ${page.second}")
                val watchlistIds = traktSource.getWatchlistMovies(page.second.page).bind()
                watchlistIds.map { getMovieDetails(it).bind().movie }
            } else {
                PagedData.Remote(emptyList(), page.second)
            }
            mergePagedData(
                first = fromTmdb,
                second = fromTrakt,
                id = { movie -> movie.tmdbId },
                onConflict = { first, _ -> first }
            )
        }

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
}
