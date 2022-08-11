package cinescout.movies.data.remote.tmdb.model

import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetMovieKeywords {

    @Serializable
    data class Response(

        @SerialName(Keywords)
        val keywords: List<Keyword>,

        @SerialName(MovieId)
        val movieId: TmdbMovieId
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

            const val MovieId = "id"
            const val Keywords = "keywords"
        }
    }
}
