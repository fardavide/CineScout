package cinescout.movies.data.remote.model

import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
