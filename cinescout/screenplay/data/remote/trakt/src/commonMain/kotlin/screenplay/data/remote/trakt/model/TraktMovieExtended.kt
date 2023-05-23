package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import korlibs.time.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesExtendedResponse = List<TraktMovieExtendedBody>

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieExtendedBody(

    @SerialName(TraktScreenplay.Ids)
    override val ids: TraktMovieIds,

    @SerialName(TraktScreenplay.Overview)
    override val overview: String = "",

    @Contextual
    @SerialName(Released)
    val releaseDate: Date?,

    @SerialName(TraktScreenplay.Title)
    val title: String,

    @SerialName(TraktScreenplay.Rating)
    override val voteAverage: Double,

    @SerialName(TraktScreenplay.Votes)
    override val voteCount: Int

) : TraktScreenplayExtendedBody {

    override val tmdbId: TmdbScreenplayId.Movie
        get() = ids.tmdb

    override val traktId: TraktScreenplayId.Movie
        get() = ids.trakt

    companion object {

        const val Released = "released"
    }
}
