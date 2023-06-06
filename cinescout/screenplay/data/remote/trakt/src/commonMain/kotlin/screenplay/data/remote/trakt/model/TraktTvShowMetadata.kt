package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktTvShowsMetadataResponse = List<TraktTvShowMetadataBody>

@Serializable
@SerialName(TraktContentType.TvShow)
data class TraktTvShowMetadataBody(
    @SerialName(TraktScreenplay.Ids)
    val ids: TraktTvShowIds
) : TraktScreenplayMetadataBody {

    override fun ids(): ScreenplayIds = TvShowIds(
        trakt = ids.trakt,
        tmdb = ids.tmdb
    )
}

/**
 * Same as [TraktTvShowMetadataBody] but with optional fields.
 */
@Serializable
data class OptTraktTvShowMetadataBody(
    @SerialName(TraktScreenplay.Ids)
    val ids: OptTraktTvShowIds
)
