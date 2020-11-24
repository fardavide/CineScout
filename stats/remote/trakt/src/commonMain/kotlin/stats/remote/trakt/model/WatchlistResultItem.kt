package stats.remote.trakt.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistResultItem(

    @SerialName("id")
    val id: Int,

    @SerialName("listed_at")
    val listedAt: String, // 2014-09-01T09:10:11.000Z

    @SerialName("movie")
    val movie: Movie,

    @SerialName("notes")
    val notes: String?,

    @SerialName("rank")
    val rank: Int, // 2

    @SerialName("type")
    val type: String // movie
)
