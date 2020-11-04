package auth.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTokenRequest(

    @SerialName("redirect_to")
    val redirectTo: String
)

@Serializable
data class RequestTokenResponse(

    @SerialName("request_token")
    val requestToken: String,

    @SerialName("status_code")
    val statusCode: Int, // 1

    @SerialName("status_message")
    val statusMessage: String, // Success.

    @SerialName("success")
    val success: Boolean // true
)
