package cinescout.movies.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.movies.data.remote.trakt.model.GetWatchlist
import cinescout.movies.data.remote.trakt.model.PostAddToWatchlist
import cinescout.movies.data.remote.trakt.model.PostRating
import cinescout.movies.data.remote.trakt.model.PostRemoveFromWatchlist
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.screenplay.domain.model.Rating
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import kotlin.math.roundToInt

internal interface TraktMovieService {

    suspend fun getRatedMovies(): Either<NetworkError, List<GetRatings.Result.Movie>>

    suspend fun getWatchlistMovies(): Either<NetworkError, List<GetWatchlist.Result.Movie>>

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit>
}

@Factory
internal class RealTraktMovieService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) : TraktMovieService {

    override suspend fun getRatedMovies(): Either<NetworkError, List<GetRatings.Result.Movie>> = Either.Try {
        client.get { url { path("sync", "ratings", "movies") } }.body()
    }

    override suspend fun getWatchlistMovies(): Either<NetworkError, List<GetWatchlist.Result.Movie>> =
        Either.Try {
            client.get { url { path("sync", "watchlist", "movies") } }.body()
        }

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        val movie = PostAddToWatchlist.Request.Movie(
            ids = PostAddToWatchlist.Request.Movie.Ids(tmdb = movieId.value.toString())
        )
        val request = PostAddToWatchlist.Request(movies = listOf(movie))
        return Either.Try {
            client.post {
                url { path("sync", "watchlist") }
                setBody(request)
            }.body()
        }
    }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> {
        val movie = PostRating.Request.Movie(
            ids = PostRating.Request.Movie.Ids(tmdb = movieId.value.toString()),
            rating = rating.value.roundToInt()
        )
        val request = PostRating.Request(movies = listOf(movie))
        return Either.Try {
            client.post {
                url.path("sync", "ratings")
                setBody(request)
            }.body()
        }
    }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        val movie = PostRemoveFromWatchlist.Request.Movie(
            ids = PostRemoveFromWatchlist.Request.Movie.Ids(tmdb = movieId.value.toString())
        )
        val request = PostRemoveFromWatchlist.Request(movies = listOf(movie))
        return Either.Try {
            client.post {
                url { path("sync", "watchlist", "remove") }
                setBody(request)
            }.body()
        }
    }
}

internal class FakeTraktMovieService : TraktMovieService {

    override suspend fun getRatedMovies(): Either<NetworkError, List<GetRatings.Result.Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchlistMovies(): Either<NetworkError, List<GetWatchlist.Result.Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

}
