package movies.remote.tmdb.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(

    @SerialName("aspect_ratio")
    val aspectRatio: Double, // 1.777777777777778

    @SerialName("file_path")
    val filePath: String, // /fygeMr16EcxJiYhdiO1LEr7iHtI.jpg

    @SerialName("height")
    val height: Int, // 720

    @SerialName("iso_639_1")
    val iso6391: String? = null, // en

    @SerialName("vote_average")
    val voteAverage: Double, // 5.312

    @SerialName("vote_count")
    val voteCount: Int, // 1

    @SerialName("width")
    val width: Int // 1280
)
