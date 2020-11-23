package profile.trakt.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SettingsResult(

    @SerialName("account")
    val account: Account,

    @SerialName("connections")
    val connections: Connections,

    @SerialName("sharing_text")
    val sharingText: SharingText,

    @SerialName("user")
    val user: User
)
