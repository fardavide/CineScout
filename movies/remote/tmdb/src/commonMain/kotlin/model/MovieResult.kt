package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResult(

    @SerialName("id")
    val id: Int, // 164558

    @SerialName("title")
    val title: String, // One Direction: This Is Us

    @SerialName("original_title")
    val originalTitle: String, // One Direction: This Is Us

    @SerialName("genre_ids")
    val genreIds: List<Int>,

    @SerialName("original_language")
    val originalLanguage: String, // en

    @SerialName("poster_path")
    val posterPath: String?, // null

    @SerialName("backdrop_path")
    val backdropPath: String?, // null

    @SerialName("video")
    val video: Boolean, // false

    @SerialName("release_date")
    val releaseDate: String, // 2013-08-30

    @SerialName("overview")
    val overview: String, // Go behind the scenes during One Directions sell out "Take Me Home" tour and experience life on the road.

    @SerialName("adult")
    val adult: Boolean, // false

    @SerialName("popularity")
    val popularity: Double, // 1.166982

    @SerialName("vote_average")
    val voteAverage: Double, // 8.45

    @SerialName("vote_count")
    val voteCount: Int // 55
)
