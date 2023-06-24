package cinescout.sync.data.remote.mapper

import cinescout.sync.data.remote.model.TraktLastActivities
import cinescout.sync.domain.model.LastActivities
import cinescout.sync.domain.model.ScreenplayCollectionLastActivities
import org.koin.core.annotation.Factory

@Factory
internal class TraktLastActivitiesMapper {

    fun toLastActivities(traktLastActivities: TraktLastActivities) = LastActivities(
        ratings = ScreenplayCollectionLastActivities(
            movies = traktLastActivities.movies.ratedAt,
            tvShows = traktLastActivities.tvShows.ratedAt
        ),
        watchlist = ScreenplayCollectionLastActivities(
            movies = traktLastActivities.movies.watchlistedAt,
            tvShows = traktLastActivities.tvShows.watchlistedAt
        )
    )
}
