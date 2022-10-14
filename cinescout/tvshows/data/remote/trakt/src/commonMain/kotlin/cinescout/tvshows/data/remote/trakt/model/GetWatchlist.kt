package cinescout.tvshows.data.remote.trakt.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface GetWatchlist {

    @Serializable
    sealed interface Result {

        @Serializable
        data class TvShow(

            @SerialName(TvShowType)
            val tvShow: TvShowBody
        ) : Result

        @Serializable
        data class TvShowBody(

            @SerialName(Result.Ids)
            val ids: Ids,

            @SerialName(Title)
            val title: String
        )

        @Serializable
        data class Ids(

            @SerialName(Tmdb)
            val tmdb: TmdbTvShowId
        ) {

            companion object {

                const val Tmdb = "tmdb"
            }
        }

        enum class Type {

            Show
        }

        companion object {

            const val Ids = "ids"
            const val TvShowType = "show"
            const val Title = "title"
        }
    }
}
