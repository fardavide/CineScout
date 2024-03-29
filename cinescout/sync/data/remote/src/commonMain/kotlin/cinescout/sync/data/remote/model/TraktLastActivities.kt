package cinescout.sync.data.remote.model

import cinescout.sync.data.remote.model.TraktLastActivities.Companion.RatedAt
import cinescout.sync.data.remote.model.TraktLastActivities.Companion.WatchedAt
import cinescout.sync.data.remote.model.TraktLastActivities.Companion.WatchlistedAt
import korlibs.time.DateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktLastActivities(

    @SerialName(Episodes)
    val episodes: TraktEpisodesLastActivities,

    @SerialName(Movies)
    val movies: TraktMoviesLastActivities,

    @SerialName(TvShows)
    val tvShows: TraktTvShowsLastActivities
) {

    companion object {

        const val Episodes = "episodes"
        const val Movies = "movies"
        const val TvShows = "shows"

        const val RatedAt = "rated_at"
        const val WatchedAt = "watched_at"
        const val WatchlistedAt = "watchlisted_at"
    }
}

@Serializable
data class TraktEpisodesLastActivities(

    @Contextual
    @SerialName(RatedAt)
    val ratedAt: DateTime,

    @Contextual
    @SerialName(WatchedAt)
    val watchedAt: DateTime,

    @Contextual
    @SerialName(WatchlistedAt)
    val watchlistedAt: DateTime
)

@Serializable
data class TraktMoviesLastActivities(

    @Contextual
    @SerialName(RatedAt)
    val ratedAt: DateTime,

    @Contextual
    @SerialName(WatchedAt)
    val watchedAt: DateTime,

    @Contextual
    @SerialName(WatchlistedAt)
    val watchlistedAt: DateTime
)

@Serializable
data class TraktTvShowsLastActivities(

    @Contextual
    @SerialName(RatedAt)
    val ratedAt: DateTime,

    @Contextual
    @SerialName(WatchlistedAt)
    val watchlistedAt: DateTime
)
