package cinescout.media.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetScreenplayImagesResponse(

    @SerialName(Backdrops)
    val backdrops: List<Backdrop>,

    @SerialName(Posters)
    val posters: List<Poster>,

    @SerialName(Id)
    val screenplayId: TmdbScreenplayId
) {

    @Serializable
    data class Backdrop(

        @SerialName(FilePath)
        val path: String
    )

    @Serializable
    data class Poster(

        @SerialName(FilePath)
        val path: String
    )

    companion object {

        const val Backdrops = "backdrops"
        const val Id = "id"
        const val FilePath = "file_path"
        const val Posters = "posters"
    }
}