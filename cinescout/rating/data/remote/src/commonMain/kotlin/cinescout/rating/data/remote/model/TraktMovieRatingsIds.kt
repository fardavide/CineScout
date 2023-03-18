package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplayType

@Serializable
@SerialName(TraktScreenplayType.Movie)
data class TraktMovieRatingIdsBody(

    @SerialName(Ids)
    val ids: TraktMovieIds,

    @SerialName(Rating)
    val rating: Int
) {

    companion object {

        const val Ids = "ids"
    }
}
