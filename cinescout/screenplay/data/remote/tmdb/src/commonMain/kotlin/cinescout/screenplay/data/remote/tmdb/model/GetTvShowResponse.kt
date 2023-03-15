package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTvShowResponse(

    @Contextual
    @SerialName(TmdbTvShow.FirstAirDate)
    val firstAirDate: Date,

    @SerialName(Genres)
    val genres: List<Genre> = emptyList(),

    @SerialName(TmdbScreenplay.Id)
    val id: TmdbScreenplayId.TvShow,

    @SerialName(TmdbTvShow.Name)
    val name: String,

    @SerialName(TmdbTvShow.Overview)
    val overview: String,

    @SerialName(TmdbTvShow.VoteAverage)
    val voteAverage: Double,

    @SerialName(TmdbTvShow.VoteCount)
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
