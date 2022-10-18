package cinescout.tvshows.data.remote.tmdb.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetTvShowKeywords {

    @Serializable
    data class Response(

        @SerialName(Results)
        val keywords: List<Keyword>,

        @SerialName(TvShowId)
        val tvShowId: TmdbTvShowId
    ) {

        @Serializable
        data class Keyword(

            @SerialName(Id)
            val id: Int,

            @SerialName(Name)
            val name: String
        ) {

            companion object {

                const val Id = "id"
                const val Name = "name"
            }
        }

        companion object {

            const val TvShowId = "id"
            const val Results = "results"
        }
    }
}
