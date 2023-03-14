package cinescout.database.mapper

import cinescout.database.model.DatabaseTvShowWithPersonalRating
import cinescout.database.tvShow.FindAllWithPersonalRating

fun List<FindAllWithPersonalRating>.groupAsTvShowWithRating(): DatabaseTvShowWithPersonalRating {
    check(groupBy { it.tmdbId }.size == 1) { "List of tv shows must be grouped by tmdbId" }
    val tvShowInfo = first()
    return DatabaseTvShowWithPersonalRating(
        firstAirDate = tvShowInfo.firstAirDate,
        overview = tvShowInfo.overview,
        ratingAverage = tvShowInfo.ratingAverage,
        personalRating = tvShowInfo.personalRating,
        ratingCount = tvShowInfo.ratingCount,
        title = tvShowInfo.title,
        tmdbId = tvShowInfo.tmdbId
    )
}

fun List<FindAllWithPersonalRating>.groupAsTvShowsWithRating(): List<DatabaseTvShowWithPersonalRating> =
    groupBy { it.tmdbId }.values.map { it.groupAsTvShowWithRating() }

