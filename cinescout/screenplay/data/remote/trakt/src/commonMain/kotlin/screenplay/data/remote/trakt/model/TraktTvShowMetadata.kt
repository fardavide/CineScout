package screenplay.data.remote.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktTvShowsMetadataResponse = List<TraktTvShowMetadataBody>

@Serializable
data class TraktTvShowMetadataBody(

    @SerialName(TraktScreenplay.Ids)
    val ids: TraktTvShowIds
)
