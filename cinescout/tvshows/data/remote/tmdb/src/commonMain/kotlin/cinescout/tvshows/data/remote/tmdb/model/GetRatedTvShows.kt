package cinescout.tvshows.data.remote.tmdb.model

import cinescout.tvshows.domain.model.TmdbTvShowId
import com.soywiz.klock.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface GetRatedTvShows {

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

            @SerialName(TmdbTvShow.BackdropPath)
            val backdropPath: String?,

            @SerialName(TmdbTvShow.FirstAirDate)
            @Contextual
            val firstAirDate: Date,

            @SerialName(TmdbTvShow.Id)
            val id: TmdbTvShowId,

            @SerialName(TmdbTvShow.Name)
            val name: String,

            @SerialName(TmdbTvShow.Overview)
            val overview: String,

            @SerialName(TmdbTvShow.PosterPath)
            val posterPath: String?,

            @SerialName(Rating)
            val rating: Double,

            @SerialName(TmdbTvShow.VoteAverage)
            val voteAverage: Double,

            @SerialName(TmdbTvShow.VoteCount)
            val voteCount: Int
        ) {

            fun toTmdbTvShow() = TmdbTvShow(
                backdropPath = backdropPath,
                firstAirDate = firstAirDate,
                id = id,
                overview = overview,
                posterPath = posterPath,
                title = name,
                voteAverage = voteAverage,
                voteCount = voteCount
            )

            companion object {

                const val Rating = "rating"
            }
        }

        companion object {

            const val Page = "page"
            const val Results = "results"
            const val TotalPages = "total_pages"
            const val TotalResults = "total_results"
        }
    }
}
