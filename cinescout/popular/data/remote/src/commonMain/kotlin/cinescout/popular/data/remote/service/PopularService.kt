package cinescout.popular.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.withLimit
import cinescout.popular.data.remote.model.TraktMoviesPopularMetadataResponse
import cinescout.popular.data.remote.model.TraktScreenplaysPopularMetadataResponse
import cinescout.popular.data.remote.model.TraktTvShowsPopularMetadataResponse
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.utils.kotlin.plus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class PopularService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getMostPopularIds(
        type: ScreenplayType,
        limit: Int = DefaultLimit
    ): Either<NetworkError, TraktScreenplaysPopularMetadataResponse> = when (type) {
        ScreenplayType.All -> {
            coroutineScope {
                val movies = async { getMostPopularMovieIds(limit) }
                val tvShows = async { getMostPopularTvShowIds(limit) }

                movies.await() + tvShows.await()
            }
        }

        ScreenplayType.Movies -> getMostPopularMovieIds(limit)
        ScreenplayType.TvShows -> getMostPopularTvShowIds(limit)
    }

    private suspend fun getMostPopularMovieIds(
        limit: Int
    ): Either<NetworkError, TraktMoviesPopularMetadataResponse> = Either.Try {
        client.get {
            url {
                path("movies", "popular")
                withLimit(limit)
            }
        }.body()
    }

    private suspend fun getMostPopularTvShowIds(
        limit: Int
    ): Either<NetworkError, TraktTvShowsPopularMetadataResponse> = Either.Try {
        client.get {
            url {
                path("shows", "popular")
                withLimit(limit)
            }
        }.body()
    }

    companion object {

        private const val DefaultLimit = 20
    }
}
