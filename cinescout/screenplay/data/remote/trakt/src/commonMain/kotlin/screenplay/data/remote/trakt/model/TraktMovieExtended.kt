package screenplay.data.remote.trakt.model

import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesExtendedResponse = List<TraktMovieExtendedBody>

@Serializable
data class TraktMovieExtendedBody(

    @SerialName(Ids)
    val ids: TraktMovieIds,

    @SerialName(Overview)
    val overview: String,

    @Contextual
    @SerialName(Released)
    val releaseDate: Date,

    @SerialName(Title)
    val title: String,

    @SerialName(Rating)
    val voteAverage: Double,

    @SerialName(Votes)
    val voteCount: Int
) {

    companion object {

        const val Ids = "ids"
        const val Overview = "overview"
        const val Rating = "rating"
        const val Released = "released"
        const val Title = "title"
        const val Votes = "votes"
    }
}
