package movies.remote.tmdb.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguage(
    @SerialName("iso_639_1")
    val iso6391: String, // en
    @SerialName("name")
    val name: String // English
)
