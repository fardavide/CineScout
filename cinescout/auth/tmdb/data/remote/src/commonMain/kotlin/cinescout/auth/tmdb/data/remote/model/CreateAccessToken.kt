package cinescout.auth.tmdb.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface CreateAccessToken {

    @Serializable
    data class Request(

        @SerialName(RequestToken)
        val requestToken: String
    ) {

        companion object {

            const val RequestToken = "request_token"
        }
    }

    @Serializable
    data class Response(

        @SerialName(AccessToken)
        val accessToken: String,

        @SerialName(AccountId)
        val accountId: String
    ) {

        companion object {

            const val AccessToken = "access_token"
            const val AccountId = "account_id"
        }
    }
}
