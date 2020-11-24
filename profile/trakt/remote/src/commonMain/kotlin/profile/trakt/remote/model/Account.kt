package profile.trakt.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Account(

    @SerialName("cover_image")
    val coverImage: String?, // https://walter.trakt.tv/images/movies/000/001/545/fanarts/original/0abb604492.jpg

    @SerialName("date_format")
    val dateFormat: String, // mdy

    @SerialName("time_24hr")
    val time24hr: Boolean, // false

    @SerialName("timezone")
    val timezone: String, // America/Los_Angeles

    @SerialName("token")
    val token: String?
)
