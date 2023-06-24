package cinescout.sync.domain.model

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import korlibs.time.DateTime
import korlibs.time.max

data class LastActivities(
    val ratings: ScreenplayCollectionLastActivities,
    val watchlist: ScreenplayCollectionLastActivities
)

data class ScreenplayCollectionLastActivities(
    val movies: DateTime,
    val tvShows: DateTime
)

operator fun ScreenplayCollectionLastActivities.get(typeFilter: ScreenplayTypeFilter): DateTime =
    when (typeFilter) {
        ScreenplayTypeFilter.All -> max(movies, tvShows)
        ScreenplayTypeFilter.Movies -> movies
        ScreenplayTypeFilter.TvShows -> tvShows
    }
