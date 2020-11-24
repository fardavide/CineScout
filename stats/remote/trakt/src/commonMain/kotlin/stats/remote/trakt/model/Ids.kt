package stats.remote.trakt.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ids(

    @SerialName("imdb")
    val imdb: String, // tt0468569

    @SerialName("slug")
    val slug: String, // the-dark-knight-2008

    @SerialName("tmdb")
    val tmdb: Int, // 155

    @SerialName("trakt")
    val trakt: Int // 6
)
