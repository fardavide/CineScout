package cinescout.movies.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class TmdbMovieId(val value: Int)

@Serializable
data class TmdbMovie(

    @SerialName(Id)
    val id: TmdbMovieId,

    @SerialName(Title)
    val title: String
) {

    companion object {

        const val Id = "id"
        const val Title = "title"
    }
}
