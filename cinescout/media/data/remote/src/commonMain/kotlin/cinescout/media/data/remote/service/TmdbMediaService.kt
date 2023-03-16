package cinescout.media.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.media.data.remote.model.GetScreenplayImagesResponse
import cinescout.media.data.remote.model.GetScreenplayImagesResponseWithId
import cinescout.media.data.remote.model.GetScreenplayVideosResponse
import cinescout.media.data.remote.model.GetScreenplayVideosResponseWithId
import cinescout.media.data.remote.model.withId
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.screenplay.domain.model.TmdbScreenplayId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TmdbMediaService(
    @Named(TmdbNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getImages(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, GetScreenplayImagesResponseWithId> = Either.Try {
        val type = when (screenplayId) {
            is TmdbScreenplayId.Movie -> "movie"
            is TmdbScreenplayId.TvShow -> "tv"
        }
        client.get { url.path(type, screenplayId.value.toString(), "images") }
            .body<GetScreenplayImagesResponse>() withId screenplayId
    }

    suspend fun getVideos(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, GetScreenplayVideosResponseWithId> = Either.Try {
        val type = when (screenplayId) {
            is TmdbScreenplayId.Movie -> "movie"
            is TmdbScreenplayId.TvShow -> "tv"
        }
        client.get { url.path(type, screenplayId.value.toString(), "videos") }
            .body<GetScreenplayVideosResponse>() withId screenplayId
    }
}
