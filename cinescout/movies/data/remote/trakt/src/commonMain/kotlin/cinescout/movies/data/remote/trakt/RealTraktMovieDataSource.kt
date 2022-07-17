package cinescout.movies.data.remote.trakt

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.store.PagedData
import io.ktor.client.HttpClient

class RealTraktMovieDataSource(
    private val client: HttpClient
) : TraktRemoteMovieDataSource {

    override suspend fun getRatedMovies(): Either<NetworkError, PagedData.Remote<MovieWithRating>> {
        TODO("Not yet implemented")
    }

    override suspend fun postRating(movie: Movie, rating: Rating) {
    }

    override suspend fun postWatchlist(movie: Movie) {
    }
}
