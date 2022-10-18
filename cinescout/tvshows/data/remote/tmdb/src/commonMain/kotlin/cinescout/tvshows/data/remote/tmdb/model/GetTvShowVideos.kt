package cinescout.tvshows.data.remote.tmdb.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetTvShowVideos {

    @Serializable
    data class Response(

        @SerialName(Results)
        val posters: List<Video>,

        @SerialName(TvShowId)
        val tvShowId: TmdbTvShowId
    ) {

        @Serializable
        data class Video(

            @SerialName(Id)
            val id: String,

            @SerialName(Key)
            val key: String,

            @SerialName(Name)
            val name: String,

            @SerialName(Site)
            val site: String,

            @SerialName(Size)
            val size: Int,

            @SerialName(Type)
            val type: String
        ) {

            companion object {

                const val Id = "id"
                const val Key = "key"
                const val Name = "name"
                const val Site = "site"
                const val Size = "size"
                const val Type = "type"
            }
        }

        companion object {

            const val Id = "id"
            const val TvShowId = "id"
            const val Results = "results"
        }
    }
}
