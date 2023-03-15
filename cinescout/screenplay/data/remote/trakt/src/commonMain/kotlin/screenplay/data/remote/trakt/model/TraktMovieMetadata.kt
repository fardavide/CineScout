package screenplay.data.remote.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesMetadataResponse = List<TraktMovieMetadataBody>

@Serializable
data class TraktMovieMetadataBody(

    @SerialName(TraktScreenplay.Ids)
    val ids: TraktMovieIds
)
