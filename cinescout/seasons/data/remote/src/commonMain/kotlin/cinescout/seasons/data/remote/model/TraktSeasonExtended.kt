package cinescout.seasons.data.remote.model

import cinescout.screenplay.domain.model.SeasonNumber
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContent
import screenplay.data.remote.trakt.model.TraktSeasonIds

typealias TraktSeasonsExtendedWithEpisodesResponse = List<TraktSeasonExtendedWithEpisodesBody>

@Serializable
data class TraktSeasonExtendedWithEpisodesBody(

    @Contextual
    @SerialName(TraktEpisodeExtendedBody.FirstAired)
    val firstAirDate: DateTime? = null,

    @SerialName(TraktContent.Ids)
    val ids: TraktSeasonIds,

    @SerialName(EpisodeCount)
    val episodeCount: Int,

    @SerialName(Episodes)
    val episodes: List<TraktEpisodeExtendedBody>,

    @SerialName(Number)
    val number: SeasonNumber,

    @SerialName(TraktContent.Overview)
    val overview: String?,

    @SerialName(TraktContent.Title)
    val title: String,

    @SerialName(TraktContent.Rating)
    val voteAverage: Double,

    @SerialName(TraktContent.Votes)
    val voteCount: Int

) {

    companion object {

        const val EpisodeCount = "episode_count"
        const val Episodes = "episodes"
        const val Number = "number"
    }
}
