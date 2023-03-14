package cinescout.details.data.remote.model

import cinescout.screenplay.data.remote.tmdb.model.TmdbMovie
import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMovieDetailsResponse(

    @SerialName(Genres)
    val genres: List<Genre> = emptyList(),

    @SerialName(TmdbScreenplay.Id)
    val id: TmdbScreenplayId.Movie,

    @SerialName(TmdbMovie.Overview)
    val overview: String,

    @SerialName(TmdbMovie.ReleaseDate)
    @Contextual
    val releaseDate: Date? = null,

    @SerialName(TmdbMovie.Title)
    val title: String,

    @SerialName(TmdbMovie.VoteAverage)
    val voteAverage: Double,

    @SerialName(TmdbMovie.VoteCount)
    val voteCount: Int
) {

    @Serializable
    data class Genre(

        @SerialName(Id)
        val id: Int,

        @SerialName(Name)
        val name: String
    ) {

        companion object {

            const val Id = "id"
            const val Name = "name"
        }
    }

    companion object {

        const val Genres = "genres"
    }
}
