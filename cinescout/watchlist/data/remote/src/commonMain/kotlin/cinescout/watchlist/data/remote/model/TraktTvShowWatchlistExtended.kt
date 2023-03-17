package cinescout.watchlist.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktTvShowsWatchlistExtendedResponse = List<TraktTvShowWatchlistExtendedBody>

@Serializable
data class TraktTvShowWatchlistExtendedBody(

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowExtendedBody

) : TraktScreenplayWatchlistExtendedBody {

    override val screenplay: TraktTvShowExtendedBody
        get() = tvShow
}
