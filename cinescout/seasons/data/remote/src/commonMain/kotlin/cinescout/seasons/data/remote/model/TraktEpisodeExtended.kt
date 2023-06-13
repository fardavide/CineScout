package cinescout.seasons.data.remote.model

import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContent
import screenplay.data.remote.trakt.model.TraktEpisodeIds
import kotlin.time.Duration

@Serializable
data class TraktEpisodeExtendedBody(

    @Contextual
    @SerialName(FirstAired)
    val firstAirDate: DateTime? = null,

    @SerialName(TraktContent.Ids)
    val ids: TraktEpisodeIds,

    @SerialName(Number)
    val number: EpisodeNumber,

    @SerialName(TraktContent.Overview)
    val overview: String = "",

    @Contextual
    @SerialName(TraktContent.Runtime)
    val runtime: Duration,

    @SerialName(Season)
    val seasonNumber: SeasonNumber,

    @SerialName(TraktContent.Title)
    val title: String = "",

    @SerialName(TraktContent.Rating)
    val voteAverage: Double,

    @SerialName(TraktContent.Votes)
    val voteCount: Int
) {

    companion object {

        const val FirstAired = "first_aired"
        const val Number = "number"
        const val Season = "season"
    }
}
