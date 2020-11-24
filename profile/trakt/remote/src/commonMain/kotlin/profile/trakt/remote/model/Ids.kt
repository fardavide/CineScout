package profile.trakt.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ids(
    @SerialName("slug")
    val slug: String, // justin
    @SerialName("uuid")
    val uuid: String // b6589fc6ab0dc82cf12099d1c2d40ab994e8410c
)
