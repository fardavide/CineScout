package cinescout.account.trakt.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetAccount {

    @Serializable
    data class Response(

        @SerialName(Response.User)
        val user: User
    ) {

        @Serializable
        data class User(

            @SerialName(User.Images)
            val images: Images,

            @SerialName(Username)
            val username: String
        ) {

            @Serializable
            data class Images(

                @SerialName(Images.Avatar)
                val avatar: Avatar
            ) {

                @Serializable
                data class Avatar(

                    @SerialName(Full)
                    val full: String
                ) {

                    companion object {

                        const val Full = "full"
                    }
                }

                companion object {

                    const val Avatar = "avatar"
                }
            }

            companion object {

                const val Images = "images"
                const val Username = "username"
            }
        }

        companion object {

            const val User = "user"
        }
    }
}
