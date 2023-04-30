package cinescout.popular.data.remote.service

import arrow.core.Either
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
import screenplay.data.remote.trakt.model.TraktMoviesMetadataResponse
import screenplay.data.remote.trakt.model.TraktScreenplayMetadataResponse
import screenplay.data.remote.trakt.model.TraktTvShowsMetadataResponse

@Factory
internal class PopularService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getMostPopularIds(
        type: ScreenplayTypeFilter,
        limit: Int = DefaultLimit
    ): Either<NetworkError, TraktScreenplayMetadataResponse> = when (type) {
        ScreenplayTypeFilter.All -> {
            coroutineScope {
                val movies = async { getMostPopularMovieIds(limit) }
                val tvShows = async { getMostPopularTvShowIds(limit) }

                movies.await() + tvShows.await()
            }
        }

        ScreenplayTypeFilter.Movies -> getMostPopularMovieIds(limit)
        ScreenplayTypeFilter.TvShows -> getMostPopularTvShowIds(limit)
    }

    private suspend fun getMostPopularMovieIds(
        limit: Int
    ): Either<NetworkError, TraktMoviesMetadataResponse> = Either.Try {
        client.get {
            url {
                path("movies", "popular")
                withLimit(limit)
            }
        }.body()
    }

    private suspend fun getMostPopularTvShowIds(
        limit: Int
    ): Either<NetworkError, TraktTvShowsMetadataResponse> = Either.Try {
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
