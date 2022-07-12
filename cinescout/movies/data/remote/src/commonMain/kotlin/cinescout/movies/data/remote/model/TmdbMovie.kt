package cinescout.movies.data.remote.model

import cinescout.movies.domain.model.TmdbMovieId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovie(

    @SerialName(Id)
    val id: TmdbMovieId,

    @SerialName(ReleaseDate)
    @Contextual
    val releaseDate: Date,

    @SerialName(Title)
    val title: String
) {

    companion object {

        const val Id = "id"
        const val ReleaseDate = "release_date"
        const val Title = "title"
    }
}
