package movies.remote.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Images(

    @SerialName("posters")
    val posters: List<Image>,

    @SerialName("backdrops")
    val backdrops: List<Image>
) {

    companion object {
        val Empty = Images(emptyList(), emptyList())
    }
}

