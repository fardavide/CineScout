package auth.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForkV4TokenRequest(

    @SerialName("access_token")
    val accessToken: String
)

@Serializable
data class ForkV4TokenResponse(

    @SerialName("success")
    val success: Boolean, // true

    @SerialName("session_id")
    val sessionId: String
)
