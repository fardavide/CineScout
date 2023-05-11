package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import korlibs.time.Date
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktTvShowsExtendedResponse = List<TraktTvShowExtendedBody>

@Serializable
@SerialName(TraktScreenplayType.TvShow)
data class TraktTvShowExtendedBody(

    @Contextual
    @SerialName(FirstAired)
    val firstAirDate: Date = DateTime.EPOCH.date,

    @SerialName(TraktScreenplay.Ids)
    val ids: TraktTvShowIds,

    @SerialName(Overview)
    val overview: String = "",

    @SerialName(Title)
    val title: String,

    @SerialName(Rating)
    val voteAverage: Double,

    @SerialName(Votes)
    val voteCount: Int

) : TraktScreenplayExtendedBody {

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = ids.tmdb

    override val traktId: TraktScreenplayId.TvShow
        get() = ids.trakt

    companion object {

        const val FirstAired = "first_aired"
        const val Overview = "overview"
        const val Rating = "rating"
        const val Title = "title"
        const val Votes = "votes"
    }
}
