package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ScreenplayIds
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktTvShowsMetadataResponse = List<TraktTvShowMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.TvShow)
data class TraktTvShowMetadataBody(

    @SerialName(TraktScreenplay.Ids)
    val ids: TraktTvShowIds

) : TraktScreenplayMetadataBody {

    override fun ids(): ScreenplayIds = ScreenplayIds.TvShow(
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
