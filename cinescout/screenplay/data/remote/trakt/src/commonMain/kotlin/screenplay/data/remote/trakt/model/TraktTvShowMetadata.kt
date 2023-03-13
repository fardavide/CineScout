package screenplay.data.remote.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktTvShowsMetadataResponse = List<TraktTvShowMetadataBody>

@Serializable
data class TraktTvShowMetadataBody(

    @SerialName(TraktMovieMetadataBody.Ids)
    val ids: TraktTvShowIds,

    @SerialName(Title)
    val title: String
) {

    companion object {

        const val Ids = "ids"
        const val Title = "title"
    }
}
