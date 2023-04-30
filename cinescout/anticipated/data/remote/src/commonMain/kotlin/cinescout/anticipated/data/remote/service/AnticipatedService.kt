package cinescout.anticipated.data.remote.service

import arrow.core.Either
import cinescout.anticipated.data.remote.model.TraktMoviesAnticipatedMetadataResponse
import cinescout.anticipated.data.remote.model.TraktScreenplaysAnticipatedMetadataResponse
import cinescout.anticipated.data.remote.model.TraktTvShowsAnticipatedMetadataResponse
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.withLimit
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
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
internal class AnticipatedService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getMostAnticipatedIds(
        type: ScreenplayTypeFilter,
        limit: Int = DefaultLimit
    ): Either<NetworkError, TraktScreenplaysAnticipatedMetadataResponse> = when (type) {
        ScreenplayTypeFilter.All -> {
            coroutineScope {
                val movies = async { getMostAnticipatedMovieIds(limit) }
                val tvShows = async { getMostAnticipatedTvShowIds(limit) }

                movies.await() + tvShows.await()
            }
        }

        ScreenplayTypeFilter.Movies -> getMostAnticipatedMovieIds(limit)
        ScreenplayTypeFilter.TvShows -> getMostAnticipatedTvShowIds(limit)
    }

    private suspend fun getMostAnticipatedMovieIds(
        limit: Int
    ): Either<NetworkError, TraktMoviesAnticipatedMetadataResponse> = Either.Try {
        client.get {
            url {
                path("movies", "anticipated")
                withLimit(limit)
            }
        }.body()
    }

    private suspend fun getMostAnticipatedTvShowIds(
        limit: Int
    ): Either<NetworkError, TraktTvShowsAnticipatedMetadataResponse> = Either.Try {
        client.get {
            url {
                path("shows", "anticipated")
                withLimit(limit)
            }
        }.body()
    }

    companion object {

        private const val DefaultLimit = 20
    }
}
