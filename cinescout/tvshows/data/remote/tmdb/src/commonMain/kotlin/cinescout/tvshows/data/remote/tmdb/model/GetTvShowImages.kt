package cinescout.tvshows.data.remote.tmdb.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetTvShowImages {

    @Serializable
    data class Response(

        @SerialName(Backdrops)
        val backdrops: List<Backdrop>,

        @SerialName(Posters)
        val posters: List<Poster>,

        @SerialName(TvShowId)
        val tvShowId: TmdbTvShowId
    ) {

        @Serializable
        data class Backdrop(

            @SerialName(FilePath)
            val path: String
        )

        @Serializable
        data class Poster(

            @SerialName(FilePath)
            val path: String
        )

        companion object {

            const val Backdrops = "backdrops"
            const val FilePath = "file_path"
            const val Posters = "posters"
            const val TvShowId = "id"
        }
    }
}
