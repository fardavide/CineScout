package cinescout.sync.domain.model

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import korlibs.time.DateTime
import korlibs.time.max

data class LastActivities(
    val history: ScreenplaysLastActivities,
    val ratings: ScreenplaysLastActivities,
    val watchlist: ScreenplaysLastActivities
)

data class ScreenplaysLastActivities(
    val movies: DateTime,
    val tvShows: DateTime
)

operator fun ScreenplaysLastActivities.get(typeFilter: ScreenplayTypeFilter): DateTime = when (typeFilter) {
    ScreenplayTypeFilter.All -> max(movies, tvShows)
    ScreenplayTypeFilter.Movies -> movies
    ScreenplayTypeFilter.TvShows -> tvShows
}
