package cinescout.database.model

import cinescout.database.model.id.DatabaseTmdbMovieId
import cinescout.database.model.id.DatabaseTraktMovieId
import korlibs.time.Date
import kotlin.time.Duration

data class DatabaseMovieWithPersonalRating(
    val tmdbId: DatabaseTmdbMovieId,
    val traktId: DatabaseTraktMovieId,
    val overview: String,
    val personalRating: Double,
    val ratingAverage: Double,
    val ratingCount: Long,
    val releaseDate: Date?,
    val runtime: Duration?,
    val tagline: String?,
    val title: String
)
