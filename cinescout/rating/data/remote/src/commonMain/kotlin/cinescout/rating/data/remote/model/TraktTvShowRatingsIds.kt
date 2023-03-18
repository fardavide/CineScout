package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowIds

@Serializable
@SerialName(TraktScreenplayType.TvShow)
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
