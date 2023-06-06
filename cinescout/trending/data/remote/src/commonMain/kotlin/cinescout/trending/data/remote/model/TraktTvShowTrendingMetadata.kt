package cinescout.trending.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsTrendingMetadataResponse = List<TraktTvShowTrendingMetadataBody>

@Serializable
data class TraktTvShowTrendingMetadataBody(
    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowMetadataBody
) : TraktScreenplayTrendingMetadataBody {

    override val tmdbId: TmdbTvShowId
        get() = tvShow.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = tvShow.ids.trakt
}
