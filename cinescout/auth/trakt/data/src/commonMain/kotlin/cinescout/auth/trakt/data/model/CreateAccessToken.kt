package cinescout.auth.trakt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface CreateAccessToken {

    @Serializable
    sealed interface Request {

        val clientId: String
        val clientSecret: String
        val redirectUri: String
        val grantType: String

        @Serializable
        data class FromCode(

            @SerialName(Code)
            val code: String,

            @SerialName(ClientId)
            override val clientId: String,

            @SerialName(ClientSecret)
            override val clientSecret: String,

            @SerialName(RedirectUri)
            override val redirectUri: String,

            @SerialName(GrantType)
            override val grantType: String
        ) : Request {

            companion object {

                const val Code = "code"
            }
        }

        @Serializable
        data class FromRefreshToken(

            @SerialName(RefreshToken)
            val refreshToken: String,

            @SerialName(ClientId)
            override val clientId: String,

            @SerialName(ClientSecret)
            override val clientSecret: String,

            @SerialName(RedirectUri)
            override val redirectUri: String,

            @SerialName(GrantType)
            override val grantType: String
        ) : Request {

            companion object {

                const val RefreshToken = "refresh_token"
            }
        }

        companion object {

            const val ClientId = "client_id"
            const val ClientSecret = "client_secret"
            const val RedirectUri = "redirect_uri"
            const val GrantType = "grant_type"
        }
    }

    @Serializable
    data class Response(

        @SerialName(AccessToken)
        val accessToken: String,

        @SerialName(TokenType)
        val tokenType: String,

        @SerialName(ExpiresIn)
        val expiresIn: Long,

        @SerialName(RefreshToken)
        val refreshToken: String,

        @SerialName(Scope)
        val scope: String,

        @SerialName(CreatedAt)
        val createdAt: Long
    ) {

        companion object {

            const val AccessToken = "access_token"
            const val TokenType = "token_type"
            const val ExpiresIn = "expires_in"
            const val RefreshToken = "refresh_token"
            const val Scope = "scope"
            const val CreatedAt = "created_at"
        }
    }
}
