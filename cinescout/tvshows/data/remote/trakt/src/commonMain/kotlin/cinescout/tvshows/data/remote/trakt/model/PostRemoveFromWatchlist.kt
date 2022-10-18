package cinescout.tvshows.data.remote.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface PostRemoveFromWatchlist {

    @Serializable
    data class Request(

        @SerialName(TvShows)
        val tvShows: List<TvShow>
    ) {

        @Serializable
        data class TvShow(

            @SerialName(TvShow.Ids)
            val ids: Ids
        ) {

            @Serializable
            data class Ids(

                @SerialName(Tmdb)
                val tmdb: String
            ) {

                companion object {

                    const val Tmdb = "tmdb"
                }
            }

            companion object {

                const val Ids = "ids"
            }
        }

        companion object {

            const val TvShows = "shows"
        }
    }
}
