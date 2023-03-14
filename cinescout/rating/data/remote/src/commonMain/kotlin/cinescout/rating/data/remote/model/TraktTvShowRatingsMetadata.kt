package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsRatingsMetadataResponse = List<TraktTvShowRatingMetadataBody>

@Serializable
data class TraktTvShowRatingMetadataBody(

    @SerialName(TraktScreenplayType.TvShow)
    val movie: TraktTvShowMetadataBody,

    @SerialName(Rating)
    val rating: Int
)
