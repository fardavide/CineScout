package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import korlibs.time.Date
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
    val id: TmdbTvShowId,
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
        val id: TmdbGenreId,
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
