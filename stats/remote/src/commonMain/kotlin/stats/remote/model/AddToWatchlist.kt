package stats.remote.model

import entities.TmdbId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToWatchlistRequest(

    @SerialName("media_type")
    val mediaType: MediaType,

    @SerialName("media_id")
    val mediaId: Int,

    @SerialName("watchlist")
    val watchlist: Boolean
) {
    constructor(mediaType: MediaType, mediaId: TmdbId, action: Action): this(mediaType, mediaId.i, action.b)

    enum class Action(val b: Boolean) {
        Add(true),
        Remove(false)
    }
}

enum class MediaType {
    Movie,
    Tv
}
