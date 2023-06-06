package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.OptTraktTvShowIds
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktTvShowIds

@Serializable
@SerialName(TraktContentType.TvShow)
data class TraktTvShowRatingIdsBody(

    @SerialName(Ids)
    val ids: TraktTvShowIds,

    @SerialName(Rating)
    val rating: Int
) {

    companion object {

        const val Ids = "ids"
    }
}

/**
 * Same as [TraktTvShowRatingIdsBody] but with optional ids.
 */
@Serializable
data class OptTraktTvShowRatingIdsBody(

    @SerialName(TraktTvShowRatingIdsBody.Ids)
    val ids: OptTraktTvShowIds,

    @SerialName(Rating)
    val rating: Int
)
