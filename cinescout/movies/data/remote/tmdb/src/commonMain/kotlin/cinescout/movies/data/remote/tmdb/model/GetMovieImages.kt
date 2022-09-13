package cinescout.movies.data.remote.tmdb.model

import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetMovieImages {

    @Serializable
    data class Response(

        @SerialName(Backdrops)
        val backdrops: List<Backdrop>,

        @SerialName(MovieId)
        val movieId: TmdbMovieId,

        @SerialName(Posters)
        val posters: List<Poster>
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
            const val MovieId = "id"
            const val FilePath = "file_path"
            const val Posters = "posters"
        }
    }
}
