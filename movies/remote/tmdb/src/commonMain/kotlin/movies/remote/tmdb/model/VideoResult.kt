package movies.remote.tmdb.model


import entities.model.Video
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResult(

    @SerialName("id")
    val id: String, // 5c9294240e0a267cd516835f

    @SerialName("iso_3166_1")
    val iso31661: String, // US

    @SerialName("iso_639_1")
    val iso6391: String, // en

    @SerialName("key")
    val key: String, // BdJKm16Co6M

    @SerialName("name")
    val name: String, // Fight Club | #TBT Trailer | 20th Century FOX

    @SerialName("site")
    val site: Video.Site, // YouTube

    @SerialName("size")
    val size: Int, // 1080

    @SerialName("type")
    val type: Video.Type // Trailer
)
