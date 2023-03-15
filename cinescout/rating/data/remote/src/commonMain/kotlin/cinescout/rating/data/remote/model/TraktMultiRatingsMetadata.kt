package cinescout.rating.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktMultiRequest

@Serializable
data class TraktMultiRatingMetadataBody(

    @SerialName(TraktMultiRequest.Movies)
    val movies: List<TraktMovieRatingMetadataBody>,

    @SerialName(TraktMultiRequest.TvShows)
    val tvShows: List<TraktTvShowRatingMetadataBody>
)
