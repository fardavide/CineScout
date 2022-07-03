package cinescout.movies.data.remote.trakt

import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import io.ktor.client.HttpClient

class RealTraktMovieDataSource(
    private val client: HttpClient
) : TraktRemoteMovieDataSource {

    override suspend fun postRating(movie: Movie, rating: Rating) {
    }

    override suspend fun postWatchlist(movie: Movie) {
    }
}
