package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import korlibs.time.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbTvShow(

    @Contextual
    @SerialName(FirstAirDate)
    val firstAirDate: Date,

    @SerialName(TmdbScreenplay.Id)
    val id: TmdbScreenplayId.TvShow,

    @SerialName(Name)
    val title: String,

    @SerialName(Overview)
    val overview: String,

    @SerialName(VoteAverage)
    val voteAverage: Double,

    @SerialName(VoteCount)
    val voteCount: Int
) {

    companion object {

        const val FirstAirDate = "first_air_date"
        const val Overview = "overview"
        const val Name = "name"
        const val VoteAverage = "vote_average"
        const val VoteCount = "vote_count"
    }
}
