package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import korlibs.time.Date
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get similar Movies.
 */
@Serializable
data class GetMovieRecommendationsResponse(

    @SerialName(TmdbPage.Page)
    val page: Int,

    @SerialName(TmdbPage.Results)
    val results: List<PageResult>,

    @SerialName(TmdbPage.TotalPages)
    val totalPages: Int,

    @SerialName(TmdbPage.TotalResults)
    val totalResults: Int
) {

    @Serializable
    data class PageResult(

        @SerialName(TmdbScreenplay.Id)
        val id: TmdbScreenplayId.Movie,

        @SerialName(TmdbMovie.Overview)
        val overview: String,

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
            id = id,
            overview = overview,
            releaseDate = releaseDate,
            title = title,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }

    fun tmdbMovies(): List<TmdbMovie> = results.map { it.toTmdbMovie() }
}
