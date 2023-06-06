package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktTvShowsRatingsExtendedResponse = List<TraktTvShowRatingExtendedBody>

@Serializable
@SerialName(TraktContentType.TvShow)
data class TraktTvShowRatingExtendedBody(
    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowExtendedBody,
    @SerialName(Rating)
    override val rating: Int
) : TraktScreenplayRatingExtendedBody {

    override val screenplay: TraktTvShowExtendedBody
        get() = tvShow

    override val tmdbId: TmdbTvShowId
        get() = tvShow.tmdbId
}
