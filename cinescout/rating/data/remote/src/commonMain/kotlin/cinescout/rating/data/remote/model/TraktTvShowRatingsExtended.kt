package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktTvShowsRatingsExtendedResponse = List<TraktTvShowRatingExtendedBody>

@Serializable
data class TraktTvShowRatingExtendedBody(

    @SerialName(TraktScreenplayType.TvShow)
    val movie: TraktTvShowExtendedBody,

    @SerialName(Rating)
    val rating: Int
)
