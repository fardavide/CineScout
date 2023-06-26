package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TvShowStatus
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.TmdbTvShowId
import cinescout.screenplay.domain.model.id.TraktTvShowId
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

    @SerialName(TraktContent.Genres)
    override val genreSlugs: List<GenreSlug>,

    @SerialName(TraktContent.Ids)
    override val ids: TraktTvShowIds,

    @SerialName(TraktContent.Overview)
    override val overview: String = "",

    @Contextual
    @SerialName(TraktContent.Runtime)
    override val runtime: Duration? = null,

    @SerialName(Status)
    val status: TvShowStatus = TvShowStatus.Rumored,

    @SerialName(TraktContent.Title)
    val title: String,

    @SerialName(TraktContent.Rating)
    override val voteAverage: Double,

    @SerialName(TraktContent.Votes)
    override val voteCount: Int

) : TraktScreenplayExtendedBody {

    override val tmdbId: TmdbTvShowId
        get() = ids.tmdb

    override val traktId: TraktTvShowId
        get() = ids.trakt

    companion object {

        const val AiredEpisodes = "aired_episodes"
        const val FirstAired = "first_aired"
        const val Status = "status"
    }
}
