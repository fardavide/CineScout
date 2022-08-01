package cinescout.account.tmdb.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetAccount {

    @Serializable
    data class Response(

        @SerialName(Response.Avatar)
        val avatar: Avatar?,

        @SerialName(Username)
        val username: String
    ) {

        @Serializable
        data class Avatar(

            @SerialName(Avatar.Gravatar)
            val gravatar: Gravatar
        ) {

            @Serializable
            data class Gravatar(

                @SerialName(Hash)
                val hash: String
            ) {

                companion object {

                    const val Hash = "hash"
                }
            }

            companion object {

                const val Gravatar = "gravatar"
            }
        }

        companion object {

            const val Avatar = "avatar"
            const val Username = "username"
        }
    }
}
