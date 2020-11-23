package profile.trakt.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharingText(
    @SerialName("rated")
    val rated: String, // [item] [stars]
    @SerialName("watched")
    val watched: String, // I just watched [item]
    @SerialName("watching")
    val watching: String // I'm watching [item]
)
