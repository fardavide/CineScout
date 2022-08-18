package cinescout.movies.data.remote.tmdb.model

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.model.TmdbMovieId
import com.soywiz.klock.Date
import com.soywiz.klock.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface DiscoverMovies {

    @Serializable
    data class Response(

        @SerialName(Page)
        val page: Int,

        @SerialName(Results)
        val results: List<PageResult>,

        @SerialName(TotalPages)
        val totalPages: Int,

        @SerialName(TotalResults)
        val totalResults: Int
    ) {

        @Serializable
        data class PageResult(

            @SerialName(TmdbMovie.BackdropPath)
            val backdropPath: String?,

            @SerialName(TmdbMovie.Id)
            val id: TmdbMovieId,

            @SerialName(TmdbMovie.PosterPath)
            val posterPath: String?,

            @Contextual
            @SerialName(TmdbMovie.ReleaseDate)
            val releaseDate: Date? = DateTime.EPOCH.date,

            @SerialName(TmdbMovie.Title)
            val title: String,

            @SerialName(TmdbMovie.VoteAverage)
            val voteAverage: Double,

            @SerialName(TmdbMovie.VoteCount)
            val voteCount: Int
        ) {

            fun toTmdbMovie() = TmdbMovie(
                backdropPath = backdropPath,
                id = id,
                posterPath = posterPath,
                releaseDate = releaseDate,
                title = title,
                voteAverage = voteAverage,
                voteCount = voteCount
            )
        }

        fun tmdbMovies(): List<TmdbMovie> =
            results.map { it.toTmdbMovie() }

        companion object {

            const val Page = "page"
            const val Results = "results"
            const val TotalPages = "total_pages"
            const val TotalResults = "total_results"
        }
    }
}
