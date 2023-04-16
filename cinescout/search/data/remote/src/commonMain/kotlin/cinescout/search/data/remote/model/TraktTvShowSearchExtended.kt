package cinescout.search.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktTvShowSearchExtendedResponse = List<TraktScreenplaySearchExtendedBody>

@Serializable
data class TraktTvShowSearchExtendedBody(

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowExtendedBody

) : TraktScreenplaySearchExtendedBody {

    override val screenplay: TraktTvShowExtendedBody
        get() = tvShow

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = tvShow.tmdbId
}
