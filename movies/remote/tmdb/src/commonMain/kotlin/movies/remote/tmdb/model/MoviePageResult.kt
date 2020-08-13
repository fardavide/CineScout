package movies.remote.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePageResult(

    @SerialName("page")
    val page: Int, // 1

    @SerialName("results")
    val results: List<MovieResult>,

    @SerialName("total_pages")
    val totalPages: Int, // 4

    @SerialName("total_results")
    val totalResults: Int // 61
)
