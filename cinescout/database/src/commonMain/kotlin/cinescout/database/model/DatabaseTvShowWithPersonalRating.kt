package cinescout.database.model

import korlibs.time.Date
import kotlin.time.Duration

data class DatabaseTvShowWithPersonalRating(
    val tmdbId: DatabaseTmdbTvShowId,
    val traktId: DatabaseTraktTvShowId,
    val firstAirDate: Date,
    val overview: String,
    val personalRating: Double,
    val ratingAverage: Double,
    val ratingCount: Long,
    val runtime: Duration?,
    val title: String
)
