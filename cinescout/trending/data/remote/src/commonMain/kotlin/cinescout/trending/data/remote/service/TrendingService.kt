package cinescout.trending.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.withLimit
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.trending.data.remote.model.TraktMoviesTrendingMetadataResponse
import cinescout.trending.data.remote.model.TraktScreenplaysTrendingMetadataResponse
import cinescout.trending.data.remote.model.TraktTvShowsTrendingMetadataResponse
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
internal class TrendingService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getMostTrendingIds(
        type: ScreenplayType,
        limit: Int = DefaultLimit
    ): Either<NetworkError, TraktScreenplaysTrendingMetadataResponse> = when (type) {
        ScreenplayType.All -> {
            coroutineScope {
                val movies = async { getMostTrendingMovieIds(limit) }
                val tvShows = async { getMostTrendingTvShowIds(limit) }

                movies.await() + tvShows.await()
            }
        }

        ScreenplayType.Movies -> getMostTrendingMovieIds(limit)
        ScreenplayType.TvShows -> getMostTrendingTvShowIds(limit)
    }

    private suspend fun getMostTrendingMovieIds(
        limit: Int
    ): Either<NetworkError, TraktMoviesTrendingMetadataResponse> = Either.Try {
        client.get {
            url {
                path("movies", "trending")
                withLimit(limit)
            }
        }.body()
    }

    private suspend fun getMostTrendingTvShowIds(
        limit: Int
    ): Either<NetworkError, TraktTvShowsTrendingMetadataResponse> = Either.Try {
        client.get {
            url {
                path("shows", "trending")
                withLimit(limit)
            }
        }.body()
    }

    companion object {

        private const val DefaultLimit = 20
    }
}
