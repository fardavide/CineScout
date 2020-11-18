package profile.tmdb.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AccountResult(

    @SerialName("avatar")
    val avatar: Avatar,

    @SerialName("id")
    val id: Int, // 548

    @SerialName("include_adult")
    val includeAdult: Boolean, // true

    @SerialName("iso_3166_1")
    val iso31661: String, // CA

    @SerialName("iso_639_1")
    val iso6391: String, // en

    @SerialName("name")
    val name: String, // Travis Bell

    @SerialName("username")
    val username: String // travisbell
)
