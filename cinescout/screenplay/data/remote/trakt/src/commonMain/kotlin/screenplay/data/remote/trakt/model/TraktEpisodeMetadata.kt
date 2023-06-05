package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.EpisodeNumber
import cinescout.screenplay.domain.model.SeasonNumber
import cinescout.screenplay.domain.model.ids.ContentIds
import cinescout.screenplay.domain.model.ids.EpisodeIds
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktEpisodesMetadataResponse = List<TraktEpisodeMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.Episode)
data class TraktEpisodeMetadataBody(
    @SerialName(Number)
    val number: EpisodeNumber,
    @SerialName(TraktScreenplay.Ids)
    val ids: TraktEpisodeIds,
    @SerialName(Season)
    val season: SeasonNumber
) : TraktContentMetadataBody {

    override fun ids(): ContentIds = EpisodeIds(
        trakt = ids.trakt,
        tmdb = ids.tmdb
    )

    companion object {

        const val Number = "number"
        const val Season = "season"
    }
}

/**
 * Same as [TraktEpisodeMetadataBody] but with optional fields.
 */
@Serializable
data class OptTraktEpisodeMetadataBody(
    @SerialName(TraktScreenplay.Ids)
    val ids: OptTraktEpisodeIds
)