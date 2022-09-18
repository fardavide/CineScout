package cinescout.movies.data.remote.tmdb.model

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.model.TmdbMovieId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetMovieDetails {

    @Serializable
    data class Response(

        @SerialName(TmdbMovie.BackdropPath)
        val backdropPath: String? = null,

        @SerialName(Genres)
        val genres: List<Genre> = emptyList(),

        @SerialName(TmdbMovie.Id)
        val id: TmdbMovieId,

        @SerialName(TmdbMovie.Overview)
        val overview: String,

        @SerialName(TmdbMovie.PosterPath)
        val posterPath: String? = null,

        @SerialName(TmdbMovie.ReleaseDate)
        @Contextual
        val releaseDate: Date? = null,

        @SerialName(TmdbMovie.Title)
        val title: String,

        @SerialName(TmdbMovie.VoteAverage)
        val voteAverage: Double,

        @SerialName(TmdbMovie.VoteCount)
        val voteCount: Int
    ) {

        @Serializable
        data class Genre(

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

            const val Genres = "genres"
        }
    }
}
