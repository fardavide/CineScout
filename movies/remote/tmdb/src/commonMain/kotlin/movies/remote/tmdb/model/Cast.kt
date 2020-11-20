package movies.remote.tmdb.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cast(

    @SerialName("adult")
    val adult: Boolean? = null,

    @SerialName("cast_id")
    val castId: Int, // 17

    @SerialName("character")
    val character: String, // Rick Flag

    @SerialName("credit_id")
    val creditId: String, // 554388d6c3a3680cd7001ef5

    @SerialName("gender")
    val gender: Int, // 2

    @SerialName("id")
    val id: Int, // 92404

    @SerialName("known_for_department")
    val knownFoDepartment: String? = null,

    @SerialName("name")
    val name: String, // Joel Kinnaman

    @SerialName("original_name")
    val originalName: String? = null,

    @SerialName("order")
    val order: Int, // 0

    @SerialName("popularity")
    val popularity: Double? = null,

    @SerialName("profile_path")
    val profilePath: String? // /45CWHmi09y7s7fBnkjtkEgeBr99.jpg
)
