package movies.remote.tmdb.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Crew(

    @SerialName("adult")
    val adult: Boolean? = null,

    @SerialName("credit_id")
    val creditId: String, // 58415a5a9251417d590062b1

    @SerialName("department")
    val department: String, // Production

    @SerialName("gender")
    val gender: Int, // 0

    @SerialName("id")
    val id: Int, // 282

    @SerialName("job")
    val job: String, // Producer

    @SerialName("known_for_department")
    val knownFoDepartment: String? = null,

    @SerialName("name")
    val name: String, // Charles Roven

    @SerialName("original_name")
    val originalName: String? = null,

    @SerialName("popularity")
    val popularity: Double? = null,

    @SerialName("profile_path")
    val profilePath: String? // /4uJLoVstC1CBcArXFOe53N2fDr1.jpg
)
