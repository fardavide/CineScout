package cinescout.movies.data.remote.trakt.model

import cinescout.movies.domain.model.TmdbMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface GetRatings {

    @Serializable
    sealed interface Result {

        val rating: Double

        @Serializable
        data class Movie(

            @SerialName(Rating)
            override val rating: Double,

            @SerialName(MovieType)
            val movie: MovieBody
        ) : Result

        @Serializable
        data class MovieBody(

            @SerialName(Result.Ids)
            val ids: Ids,

            @SerialName(Title)
            val title: String
        )

        @Serializable
        data class Ids(

            @SerialName(Tmdb)
            val tmdb: TmdbMovieId
        ) {

            companion object {

                const val Tmdb = "tmdb"
            }
        }

        enum class Type {

            Movie
        }

        companion object {

            const val Ids = "ids"
            const val MovieType = "movie"
            const val Rating = "rating"
            const val Title = "title"
        }
    }
}
