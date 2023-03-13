package screenplay.data.remote.trakt.model

import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktTvShowsExtendedResponse = List<TraktTvShowExtendedBody>

@Serializable
data class TraktTvShowExtendedBody(

    @Contextual
    @SerialName(FirstAired)
    val firstAirDate: Date,

    @SerialName(Ids)
    val ids: TraktTvShowIds,

    @SerialName(Overview)
    val overview: String,

    @SerialName(Title)
    val title: String,

    @SerialName(Rating)
    val voteAverage: Double,

    @SerialName(Votes)
    val voteCount: Int
) {

    companion object {

        const val FirstAired = "first_aired"
        const val Ids = "ids"
        const val Overview = "overview"
        const val Rating = "rating"
        const val Title = "title"
        const val Votes = "votes"
    }
}
