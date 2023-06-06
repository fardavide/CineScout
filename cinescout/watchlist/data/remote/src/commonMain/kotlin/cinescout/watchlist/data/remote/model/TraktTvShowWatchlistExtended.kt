package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktTvShowsWatchlistExtendedResponse = List<TraktTvShowWatchlistExtendedBody>

@Serializable
data class TraktTvShowWatchlistExtendedBody(
    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowExtendedBody
) : TraktScreenplayWatchlistExtendedBody {

    override val screenplay: TraktTvShowExtendedBody
        get() = tvShow

    override val tmdbId: TmdbTvShowId
        get() = tvShow.tmdbId
}
