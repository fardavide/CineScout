package stats.remote.trakt.model

import entities.TmdbId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToWatchlistRequest(

    @SerialName("movies")
    val movies: List<Movie>
) {

    constructor(tmdbId: TmdbId): this(listOf(Movie(Movie.Ids(tmdb = tmdbId.i))))

    @Serializable
    data class Movie(

        @SerialName("ids")
        val ids: Ids
    ) {

        @Serializable
        data class Ids(

            @SerialName("tmdb")
            val tmdb: Int
        )
    }
}
