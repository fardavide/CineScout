package cinescout.tvshows.data.remote.trakt.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface GetRatings {

    @Serializable
    sealed interface Result {

        val rating: Double

        @Serializable
        data class TvShow(

            @SerialName(Rating)
            override val rating: Double,

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

            TvShow
        }

        companion object {

            const val Ids = "ids"
            const val TvShowType = "show"
            const val Rating = "rating"
            const val Title = "title"
        }
    }
}
