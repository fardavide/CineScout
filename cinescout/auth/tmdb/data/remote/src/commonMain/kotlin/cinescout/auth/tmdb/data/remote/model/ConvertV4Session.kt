package cinescout.auth.tmdb.data.remote.model

import cinescout.network.tmdb.TmdbParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface ConvertV4Session {

    @Serializable
    data class Request(

        @SerialName(AccessToken)
        val accessToken: String
    ) {

        companion object {

            const val AccessToken = "access_token"
        }
    }

    @Serializable
    data class Response(

        @SerialName(TmdbParameters.SessionId)
        val sessionId: String
    )
}
