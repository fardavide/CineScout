package cinescout.movies.data.remote.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface PostAddToWatchlist {

    @Serializable
    data class Request(

        @SerialName(Movies)
        val movies: List<Movie>
    ) {

        @Serializable
        data class Movie(

            @SerialName(Movie.Ids)
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

            const val Movies = "movies"
        }
    }
}
