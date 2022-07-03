package cinescout.movies.data.remote.tmdb

import arrow.core.Either
import cinescout.movies.data.remote.TmdbRemoteMovieDataSource
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.model.TmdbMovieId
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.network.NetworkError
import cinescout.network.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

class RealTmdbMovieDataSource(
    private val client: HttpClient
) : TmdbRemoteMovieDataSource {

    override suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, TmdbMovie> =
        Either.Try {
            client.get { url.path("movie", id.value.toString()) }.body()
        }

    override suspend fun postRating(movie: Movie, rating: Rating) {
    }

    override suspend fun postWatchlist(movie: Movie) {
    }
}
