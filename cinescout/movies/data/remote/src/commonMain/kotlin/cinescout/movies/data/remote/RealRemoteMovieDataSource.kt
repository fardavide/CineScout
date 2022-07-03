package cinescout.movies.data.remote

import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll

class RealRemoteMovieDataSource(
    private val tmdbSource: TmdbRemoteMovieDataSource,
    private val traktSource: TraktRemoteMovieDataSource
) : RemoteMovieDataSource {

    override suspend fun postRating(movie: Movie, rating: Rating) = coroutineScope {
        val tmdbResult = async { tmdbSource.postRating(movie, rating) }
        val traktResult = async { traktSource.postRating(movie, rating) }
        joinAll(tmdbResult, traktResult)
    }

    override suspend fun postWatchlist(movie: Movie) = coroutineScope {
        val tmdbResult = async { tmdbSource.postWatchlist(movie) }
        val traktResult = async { traktSource.postWatchlist(movie) }
        joinAll(tmdbResult, traktResult)
    }
}
