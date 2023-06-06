package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.OptTraktMovieIds
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieIds

@Serializable
@SerialName(TraktContentType.Movie)
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

/**
 * Same as [TraktMovieRatingIdsBody] but with optional ids.
 */
@Serializable
data class OptTraktMovieRatingIdsBody(

    @SerialName(TraktMovieRatingIdsBody.Ids)
    val ids: OptTraktMovieIds,

    @SerialName(Rating)
    val rating: Int
)
