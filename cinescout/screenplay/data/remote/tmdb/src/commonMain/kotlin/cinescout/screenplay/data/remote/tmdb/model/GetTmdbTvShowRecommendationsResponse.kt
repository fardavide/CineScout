package cinescout.screenplay.data.remote.tmdb.model

import cinescout.screenplay.domain.model.id.TmdbTvShowId
import korlibs.time.Date
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Get similar Tv Shows.
 */
@Serializable
data class GetTmdbTvShowRecommendationsResponse(
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
        @Contextual
        @SerialName(TmdbTvShow.FirstAirDate)
        val firstAirDate: Date,
        @SerialName(TmdbScreenplay.Id)
        val id: TmdbTvShowId,
        @SerialName(TmdbTvShow.Overview)
        val overview: String,
        @SerialName(TmdbTvShow.Name)
        val title: String,
        @SerialName(TmdbTvShow.VoteAverage)
        val voteAverage: Double,
        @SerialName(TmdbTvShow.VoteCount)
        val voteCount: Int
    ) {

        fun toTmdbTvShow() = TmdbTvShow(
            firstAirDate = firstAirDate,
            id = id,
            overview = overview,
            title = title,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }

    fun tmdbTvShows(): List<TmdbTvShow> = results.map { it.toTmdbTvShow() }

}
