package screenplay.data.remote.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktMoviesMetadataResponse = List<TraktMovieMetadataBody>

@Serializable
data class TraktMovieMetadataBody(

    @SerialName(Ids)
    val ids: TraktMovieIds,

    @SerialName(Title)
    val title: String
) {

    companion object {

        const val Ids = "ids"
        const val Title = "title"
    }
}
