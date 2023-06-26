package cinescout.watchlist.data.remote.model

import cinescout.screenplay.domain.model.id.TmdbTvShowId
import cinescout.screenplay.domain.model.id.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsWatchlistMetadataResponse = List<TraktTvShowWatchlistMetadataBody>

@Serializable
data class TraktTvShowWatchlistMetadataBody(
    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowMetadataBody
) : TraktScreenplayWatchlistMetadataBody {

    override val tmdbId: TmdbTvShowId
        get() = tvShow.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = tvShow.ids.trakt
}
