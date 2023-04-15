package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMultiRequest

@Serializable
data class TraktMultiRatingIdsBody(

    @SerialName(TraktMultiRequest.Movies)
    val movies: List<TraktMovieRatingIdsBody>,

    @SerialName(TraktMultiRequest.TvShows)
    val tvShows: List<TraktTvShowRatingIdsBody>
)

/**
 * Same as [TraktMultiRatingIdsBody] but with optional ids.
 */
@Serializable
data class OptTraktMultiRatingIdsBody(

    @SerialName(TraktMultiRequest.Movies)
    val movies: List<OptTraktMovieRatingIdsBody>,

    @SerialName(TraktMultiRequest.TvShows)
    val tvShows: List<OptTraktTvShowRatingIdsBody>
)
