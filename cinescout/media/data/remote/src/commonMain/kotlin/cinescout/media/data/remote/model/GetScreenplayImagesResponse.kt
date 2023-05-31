package cinescout.media.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetScreenplayImagesResponse(
    @SerialName(BackdropPath)
    val backdropPath: String?,
    @SerialName(PosterPath)
    val posterPath: String?,
    @SerialName(Images)
    val images: ScreenplayImagesBody
) {

    companion object {

        const val BackdropPath = "backdrop_path"
        const val Id = "id"
        const val Images = "images"
        const val PosterPath = "poster_path"
    }
}

data class GetScreenplayImagesResponseWithId(
    val response: GetScreenplayImagesResponse,
    val screenplayId: TmdbScreenplayId
)

infix fun GetScreenplayImagesResponse.withId(id: TmdbScreenplayId) =
    GetScreenplayImagesResponseWithId(this, id)
