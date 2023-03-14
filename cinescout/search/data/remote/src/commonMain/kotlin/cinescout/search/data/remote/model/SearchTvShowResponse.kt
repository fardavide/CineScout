package cinescout.search.data.remote.model

import cinescout.screenplay.data.remote.tmdb.model.TmdbPage
import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.domain.model.TmdbScreenplayId
import com.soywiz.klock.Date
import com.soywiz.klock.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchTvShowResponse(

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
        val firstAirDate: Date = DateTime.EPOCH.date,

        @SerialName(TmdbScreenplay.Id)
        val id: TmdbScreenplayId.TvShow,

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
