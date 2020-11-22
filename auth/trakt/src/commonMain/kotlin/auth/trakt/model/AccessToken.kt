package auth.trakt.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenFromCodeRequest(

    @SerialName("client_id")
    val clientId: String, // 9b36d8c0db59eff5038aea7a417d73e69aea75b41aac771816d2ef1b3109cc2f

    @SerialName("client_secret")
    val clientSecret: String, // d6ea27703957b69939b8104ed4524595e210cd2e79af587744a7eb6e58f5b3d2

    @SerialName("code")
    val code: String, // fd0847dbb559752d932dd3c1ac34ff98d27b11fe2fea5a864f44740cd7919ad0

    @SerialName("grant_type")
    val grantType: String, // authorization_code

    @SerialName("redirect_uri")
    val redirectUri: String // urn:ietf:wg:oauth:2.0:oob
)

@Serializable
data class AccessTokenRefreshTokenRequest(

    @SerialName("client_id")
    val clientId: String, // 9b36d8c0db59eff5038aea7a417d73e69aea75b41aac771816d2ef1b3109cc2f

    @SerialName("client_secret")
    val clientSecret: String, // d6ea27703957b69939b8104ed4524595e210cd2e79af587744a7eb6e58f5b3d2

    @SerialName("refresh_token")
    val refreshToken: String, // fd0847dbb559752d932dd3c1ac34ff98d27b11fe2fea5a864f44740cd7919ad0

    @SerialName("grant_type")
    val grantType: String, // authorization_code

    @SerialName("redirect_uri")
    val redirectUri: String // urn:ietf:wg:oauth:2.0:oob
)

@Serializable
data class AccessTokenResponse(

    @SerialName("access_token")
    val accessToken: String, // dbaf9757982a9e738f05d249b7b5b4a266b3a139049317c4909f2f263572c781

    @SerialName("created_at")
    val createdAt: Int, // 1487889741

    @SerialName("expires_in")
    val expiresIn: Int, // 7200

    @SerialName("refresh_token")
    val refreshToken: String, // 76ba4c5c75c96f6087f58a4de10be6c00b29ea1ddc3b2022ee2016d1363e3a7c

    @SerialName("scope")
    val scope: String, // public

    @SerialName("token_type")
    val tokenType: String // bearer
)
