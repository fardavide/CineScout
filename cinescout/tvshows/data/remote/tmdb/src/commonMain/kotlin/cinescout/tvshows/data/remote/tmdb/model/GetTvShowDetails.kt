package cinescout.tvshows.data.remote.tmdb.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetTvShowDetails {

    @Serializable
    data class Response(

        @SerialName(TmdbTvShow.BackdropPath)
        val backdropPath: String? = null,

        @Contextual
        @SerialName(TmdbTvShow.FirstAirDate)
        val firstAirDate: Date,

        @SerialName(Genres)
        val genres: List<Genre> = emptyList(),

        @SerialName(TmdbTvShow.Id)
        val id: TmdbTvShowId,

        @SerialName(TmdbTvShow.Name)
        val name: String,

        @SerialName(TmdbTvShow.Overview)
        val overview: String,

        @SerialName(TmdbTvShow.PosterPath)
        val posterPath: String? = null,

        @SerialName(TmdbTvShow.VoteAverage)
        val voteAverage: Double,

        @SerialName(TmdbTvShow.VoteCount)
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
