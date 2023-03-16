package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesExtendedResponse = List<TraktMovieExtendedBody>

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieExtendedBody(

    @SerialName(TraktScreenplay.Ids)
    val ids: TraktMovieIds,

    @SerialName(Overview)
    val overview: String,

    @Contextual
    @SerialName(Released)
    val releaseDate: Date?,

    @SerialName(Title)
    val title: String,

    @SerialName(Rating)
    val voteAverage: Double,

    @SerialName(Votes)
    val voteCount: Int
) : TraktScreenplayExtendedBody {

    override val tmdbId: TmdbScreenplayId.Movie
        get() = ids.tmdb

    companion object {
        const val Overview = "overview"
        const val Rating = "rating"
        const val Released = "released"
        const val Title = "title"
        const val Votes = "votes"
    }
}
