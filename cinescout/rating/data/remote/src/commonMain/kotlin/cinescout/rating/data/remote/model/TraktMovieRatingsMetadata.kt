package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktScreenplayType

typealias TraktMoviesRatingsMetadataResponse = List<TraktMovieRatingMetadataBody>

@Serializable
data class TraktMovieRatingMetadataBody(

    @SerialName(TraktScreenplayType.Movie)
    val movie: TraktMovieMetadataBody,

    @SerialName(Rating)
    val rating: Int
)
