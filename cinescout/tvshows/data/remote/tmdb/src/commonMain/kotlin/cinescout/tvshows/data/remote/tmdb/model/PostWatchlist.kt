package cinescout.tvshows.data.remote.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface PostWatchlist {

    @Serializable
    data class Request(

        @SerialName(Watchlist)
        val shouldBeInWatchlist: Boolean,

        @SerialName(MediaId)
        val mediaId: String,

        @SerialName(MediaType)
        val mediaType: String
    ) {

        companion object {

            const val MediaId = "media_id"
            const val MediaType = "media_type"
            const val Watchlist = "watchlist"
        }
    }
}
