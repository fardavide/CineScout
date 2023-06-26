package cinescout.media.data.remote.model

import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetScreenplayVideosResponse(
    @SerialName(Results)
    val videos: List<Video>
) {

    @Serializable
    data class Video(
        @SerialName(Id)
        val id: String,
        @SerialName(Key)
        val key: String,
        @SerialName(Name)
        val name: String,
        @SerialName(Site)
        val site: String,
        @SerialName(Size)
        val size: Int,
        @SerialName(Type)
        val type: String
    ) {

        companion object {

            const val Id = "id"
            const val Key = "key"
            const val Name = "name"
            const val Site = "site"
            const val Size = "size"
            const val Type = "type"
        }
    }

    companion object {

        const val Id = "id"
        const val Results = "results"
    }
}

data class GetScreenplayVideosResponseWithId(
    val response: GetScreenplayVideosResponse,
    val screenplayId: TmdbScreenplayId
)

infix fun GetScreenplayVideosResponse.withId(id: TmdbScreenplayId) =
    GetScreenplayVideosResponseWithId(this, id)
