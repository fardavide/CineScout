package cinescout.search.data.remote.model

import cinescout.screenplay.domain.model.id.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktTvShowSearchExtendedResponse = List<TraktScreenplaySearchExtendedBody>

@Serializable
data class TraktTvShowSearchExtendedBody(
    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowExtendedBody
) : TraktScreenplaySearchExtendedBody {

    override val screenplay: TraktTvShowExtendedBody
        get() = tvShow

    override val tmdbId: TmdbTvShowId
        get() = tvShow.tmdbId
}
