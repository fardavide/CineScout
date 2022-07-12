package cinescout.movies.data.remote.tmdb.model

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.model.TmdbMovieId
import com.soywiz.klock.Date
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

            @SerialName(TmdbMovie.Id)
            val id: TmdbMovieId,

            @Contextual
            @SerialName(TmdbMovie.ReleaseDate)
            val releaseDate: Date,

            @SerialName(TmdbMovie.Title)
            val title: String
        ) {

            fun toTmdbMovie() = TmdbMovie(
                id = id,
                releaseDate = releaseDate,
                title = title
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
