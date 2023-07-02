package cinescout.sync.data.remote.mapper

import cinescout.sync.data.remote.model.TraktLastActivities
import cinescout.sync.domain.model.LastActivities
import cinescout.sync.domain.model.ScreenplaysLastActivities
import org.koin.core.annotation.Factory

@Factory
internal class TraktLastActivitiesMapper {

    fun toLastActivities(traktLastActivities: TraktLastActivities) = LastActivities(
        history = ScreenplaysLastActivities(
            movies = traktLastActivities.movies.watchedAt,
            tvShows = traktLastActivities.episodes.watchedAt
        ),
        ratings = ScreenplaysLastActivities(
            movies = traktLastActivities.movies.ratedAt,
            tvShows = traktLastActivities.tvShows.ratedAt
        ),
        watchlist = ScreenplaysLastActivities(
            movies = traktLastActivities.movies.watchlistedAt,
            tvShows = traktLastActivities.tvShows.watchlistedAt
        )
    )
}
