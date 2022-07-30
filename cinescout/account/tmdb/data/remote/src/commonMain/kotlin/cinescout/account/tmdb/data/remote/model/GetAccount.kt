package cinescout.account.tmdb.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetAccount {

    @Serializable
    data class Response(

        @SerialName(Username)
        val username: String
    ) {
        companion object {

            const val Username = "username"
        }
    }
}
