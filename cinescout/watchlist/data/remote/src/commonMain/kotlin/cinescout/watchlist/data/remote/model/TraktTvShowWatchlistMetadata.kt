package cinescout.watchlist.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsWatchlistMetadataResponse = List<TraktTvShowWatchlistMetadataBody>

@Serializable
data class TraktTvShowWatchlistMetadataBody(

    @SerialName(TraktScreenplayType.TvShow)
    val movie: TraktTvShowMetadataBody
)
