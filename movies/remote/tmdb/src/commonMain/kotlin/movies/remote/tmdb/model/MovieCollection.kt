package movies.remote.tmdb.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCollection(
    @SerialName("backdrop_path")
    val backdropPath: String?, // /aOQhajGvWesR386ISKwuxCUtzZJ.jpg
    @SerialName("id")
    val id: Int, // 531242
    @SerialName("name")
    val name: String, // Suicide Squad Collection
    @SerialName("poster_path")
    val posterPath: String // /bdgaCpdDh0J0H7ZRpDP2NJ8zxJE.jpg
)
