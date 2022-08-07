package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import cinescout.error.NetworkError
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.getOrThrow
import cinescout.network.DualSourceCall
import cinescout.store.PagedData
import cinescout.store.Paging
import cinescout.store.mergePagedData

class RealRemoteMovieDataSource(
    private val dualSourceCall: DualSourceCall,
    private val isTmdbLinked: IsTmdbLinked,
    private val isTraktLinked: IsTraktLinked,
    private val tmdbSource: TmdbRemoteMovieDataSource,
    private val traktSource: TraktRemoteMovieDataSource
) : RemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        tmdbSource.discoverMovies(params)

    override suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, Movie> =
        tmdbSource.getMovie(id)

    override suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, MovieCredits> =
        tmdbSource.getMovieCredits(movieId)

    override suspend fun getRatedMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<MovieWithPersonalRating, Paging.Page.DualSources>> =
        either {
            val isTmdbLinked = isTmdbLinked()
            val isTraktLinked = isTraktLinked()
            if (isTmdbLinked.not() && isTraktLinked.not()) {
                NetworkError.Unauthorized.left().bind()
            }

            val fromTmdb = if (isTmdbLinked && page.first.isValid()) {
                tmdbSource.getRatedMovies(page.first.page).bind()
            } else {
                PagedData.Remote(emptyList(), page.first)
            }
            val fromTrakt = if (isTraktLinked && page.second.isValid()) {
                val ratingWithIds = traktSource.getRatedMovies(page.second.page).bind()
                ratingWithIds.map { MovieWithPersonalRating(movie = getMovie(it.tmdbId).bind(), rating = it.rating) }
            } else {
                PagedData.Remote(emptyList(), page.second)
            }
            mergePagedData(
                first = fromTmdb,
                second = fromTrakt,
                id = { movieWithRating -> movieWithRating.movie.tmdbId },
                onConflict = { first, second ->
                    val averageRating = run {
                        val double = (first.rating.value + second.rating.value) / 2
                        Rating.of(double).getOrThrow()
                    }
                    first.copy(rating = averageRating)
                }
            )
        }

    override suspend fun postDisliked(id: TmdbMovieId): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postDisliked(id) },
            secondSourceCall = { traktSource.postDisliked(id) }
        )

    override suspend fun postLiked(id: TmdbMovieId): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postLiked(id) },
            secondSourceCall = { traktSource.postLiked(id) }
        )

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postRating(movieId, rating) },
            secondSourceCall = { traktSource.postRating(movieId, rating) }
        )

    override suspend fun postAddToWatchlist(id: TmdbMovieId): Either<NetworkError, Unit> =
        dualSourceCall(
            firstSourceCall = { tmdbSource.postAddToWatchlist(id) },
            secondSourceCall = { traktSource.postWatchlist(id) }
        )

}
