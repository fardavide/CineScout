package cinescout.tvshows.data.remote.tmdb.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbTvShow(

    @SerialName(BackdropPath)
    val backdropPath: String? = null,

    @Contextual
    @SerialName(FirstAirDate)
    val firstAirDate: Date,

    @SerialName(Id)
    val id: TmdbTvShowId,

    @SerialName(Name)
    val title: String,

    @SerialName(Overview)
    val overview: String,

    @SerialName(PosterPath)
    val posterPath: String? = null,

    @SerialName(VoteAverage)
    val voteAverage: Double,

    @SerialName(VoteCount)
    val voteCount: Int
) {

    companion object {

        const val BackdropPath = "backdrop_path"
        const val FirstAirDate = "first_air_date"
        const val Id = "id"
        const val Overview = "overview"
        const val PosterPath = "poster_path"
        const val Name = "name"
        const val VoteAverage = "vote_average"
        const val VoteCount = "vote_count"
    }
}
