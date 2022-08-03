package cinescout.movies.data.remote

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.error.NetworkError
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.getOrThrow
import cinescout.store.PagedData
import cinescout.store.Paging
import cinescout.store.mergePagedData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll

class RealRemoteMovieDataSource(
    private val tmdbSource: TmdbRemoteMovieDataSource,
    private val traktSource: TraktRemoteMovieDataSource
) : RemoteMovieDataSource {

    override suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, List<Movie>> =
        tmdbSource.discoverMovies(params)

    override suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, Movie> =
        tmdbSource.getMovie(id)

    override suspend fun getRatedMovies(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<MovieWithRating, Paging.Page.DualSources>> =
        either {
            val fromTmdb = if (page.first.isValid()) {
                tmdbSource.getRatedMovies(page.first.page).bind()
            } else {
                PagedData.Remote(emptyList(), page.first)
            }
            val fromTrakt = if (page.second.isValid()) {
                val ratingWithIds = traktSource.getRatedMovies(page.second.page).bind()
                ratingWithIds.map { MovieWithRating(movie = getMovie(it.tmdbId).bind(), rating = it.rating) }
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

    override suspend fun postRating(movie: Movie, rating: Rating): Either<NetworkError, Unit> = coroutineScope {
        val tmdbResult = async { tmdbSource.postRating(movie, rating) }
        val traktResult = async { traktSource.postRating(movie, rating) }
        tmdbResult.await().map { traktResult.await() }
    }

    override suspend fun postWatchlist(movie: Movie) = coroutineScope {
        val tmdbResult = async { tmdbSource.postWatchlist(movie) }
        val traktResult = async { traktSource.postWatchlist(movie) }
        joinAll(tmdbResult, traktResult)
    }
}
