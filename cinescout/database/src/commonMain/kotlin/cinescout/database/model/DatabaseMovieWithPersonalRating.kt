package cinescout.database.model

import com.soywiz.klock.Date

data class DatabaseMovieWithPersonalRating(
    val tmdbId: DatabaseTmdbMovieId,
    val backdropPath: String?,
    val personalRating: Double,
    val posterPath: String?,
    val ratingAverage: Double,
    val ratingCount: Long,
    val releaseDate: Date,
    val title: String
)
