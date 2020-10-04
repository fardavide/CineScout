package movies.remote.tmdb.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Videos(

    @SerialName("results")
    val results: List<VideoResult>
)
