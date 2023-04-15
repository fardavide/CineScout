package cinescout.database.model

import com.soywiz.klock.Date

data class DatabaseMovieWithPersonalRating(
    val tmdbId: DatabaseTmdbMovieId,
    val traktId: DatabaseTraktMovieId,
    val overview: String,
    val personalRating: Double,
    val ratingAverage: Double,
    val ratingCount: Long,
    val releaseDate: Date?,
    val title: String
)
