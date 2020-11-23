package profile.trakt.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Connections(
    @SerialName("apple")
    val apple: Boolean, // false
    @SerialName("facebook")
    val facebook: Boolean, // true
    @SerialName("google")
    val google: Boolean, // true
    @SerialName("medium")
    val medium: Boolean, // false
    @SerialName("slack")
    val slack: Boolean, // false
    @SerialName("tumblr")
    val tumblr: Boolean, // false
    @SerialName("twitter")
    val twitter: Boolean // true
)
