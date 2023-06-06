package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import cinescout.screenplay.domain.model.ids.TraktTvShowId
import korlibs.time.Date
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

typealias TraktTvShowsExtendedResponse = List<TraktTvShowExtendedBody>

@Serializable
@SerialName(TraktContentType.TvShow)
data class TraktTvShowExtendedBody(
    @SerialName(AiredEpisodes)
    val airedEpisodes: Int,
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

    override val tmdbId: TmdbTvShowId
        get() = ids.tmdb

    override val traktId: TraktTvShowId
        get() = ids.trakt

    companion object {

        const val AiredEpisodes = "aired_episodes"
        const val FirstAired = "first_aired"
    }
}
