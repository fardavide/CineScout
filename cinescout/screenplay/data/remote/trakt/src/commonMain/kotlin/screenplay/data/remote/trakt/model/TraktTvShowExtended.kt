package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import korlibs.time.Date
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

typealias TraktTvShowsExtendedResponse = List<TraktTvShowExtendedBody>

@Serializable
@SerialName(TraktScreenplayType.TvShow)
data class TraktTvShowExtendedBody(

    @Contextual
    @SerialName(FirstAired)
    val firstAirDate: Date = DateTime.EPOCH.date,

    @SerialName(TraktScreenplay.Ids)
    override val ids: TraktTvShowIds,

    @SerialName(TraktScreenplay.Overview)
    override val overview: String = "",

    @Contextual
    @SerialName(TraktScreenplay.Runtime)
    override val runtime: Duration? = null,

    @SerialName(TraktScreenplay.Title)
    val title: String,

    @SerialName(TraktScreenplay.Rating)
    override val voteAverage: Double,

    @SerialName(TraktScreenplay.Votes)
    override val voteCount: Int

) : TraktScreenplayExtendedBody {

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = ids.tmdb

    override val traktId: TraktScreenplayId.TvShow
        get() = ids.trakt

    companion object {

        const val FirstAired = "first_aired"
    }
}
