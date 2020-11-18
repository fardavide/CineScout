package profile.tmdb.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Avatar(

    @SerialName("gravatar")
    val gravatar: Gravatar
)
