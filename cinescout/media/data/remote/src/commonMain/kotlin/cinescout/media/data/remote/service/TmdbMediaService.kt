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
import cinescout.screenplay.data.remote.tmdb.model.toTmdbScreenplayType
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
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
        client.get {
            url {
                path(screenplayId.toTmdbScreenplayType(), screenplayId.value.toString())
                parameter("append_to_response", "images")
            }
        }.body<GetScreenplayImagesResponse>() withId screenplayId
    }

    suspend fun getVideos(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, GetScreenplayVideosResponseWithId> = Either.Try {
        val type = when (screenplayId) {
            is TmdbMovieId -> "movie"
            is TmdbTvShowId -> "tv"
        }
        client.get { url.path(type, screenplayId.value.toString(), "videos") }
            .body<GetScreenplayVideosResponse>() withId screenplayId
    }
}
