package auth.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(

    @SerialName("request_token")
    val requestToken: String
)

@Serializable
data class AccessTokenResponse(

    @SerialName("access_token")
    val accessToken: String,

    @SerialName("status_code")
    val statusCode: Int, // 1

    @SerialName("status_message")
    val statusMessage: String, // Success.

    @SerialName("success")
    val success: Boolean, // true

    @SerialName("account_id")
    val accountId: String
)
