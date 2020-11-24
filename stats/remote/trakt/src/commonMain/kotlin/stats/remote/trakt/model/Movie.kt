package stats.remote.trakt.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("ids")
    val ids: Ids,
    @SerialName("title")
    val title: String, // The Dark Knight
    @SerialName("year")
    val year: Int // 2008
)
