package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesRatingsExtendedResponse = List<TraktMovieRatingExtendedBody>

@Serializable
data class TraktMovieRatingExtendedBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieExtendedBody,

    @SerialName(Rating)
    val rating: Int
)
