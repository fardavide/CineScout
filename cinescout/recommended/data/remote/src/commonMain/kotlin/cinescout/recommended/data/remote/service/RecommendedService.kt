package cinescout.recommended.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.withLimit
import cinescout.recommended.data.remote.model.TraktMoviesRecommendedMetadataResponse
import cinescout.recommended.data.remote.model.TraktScreenplaysRecommendedMetadataResponse
import cinescout.recommended.data.remote.model.TraktTvShowsRecommendedMetadataResponse
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
internal class RecommendedService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getMostRecommendedIds(
        type: ScreenplayType,
        limit: Int = DefaultLimit
    ): Either<NetworkError, TraktScreenplaysRecommendedMetadataResponse> = when (type) {
        ScreenplayType.All -> {
            coroutineScope {
                val movies = async { getMostRecommendedMovieIds(limit) }
                val tvShows = async { getMostRecommendedTvShowIds(limit) }

                movies.await() + tvShows.await()
            }
        }

        ScreenplayType.Movies -> getMostRecommendedMovieIds(limit)
        ScreenplayType.TvShows -> getMostRecommendedTvShowIds(limit)
    }

    private suspend fun getMostRecommendedMovieIds(
        limit: Int
    ): Either<NetworkError, TraktMoviesRecommendedMetadataResponse> = Either.Try {
        client.get {
            url {
                path("movies", "popular")
                withLimit(limit)
            }
        }.body()
    }

    private suspend fun getMostRecommendedTvShowIds(
        limit: Int
    ): Either<NetworkError, TraktTvShowsRecommendedMetadataResponse> = Either.Try {
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
