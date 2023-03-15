package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

typealias TraktTvShowsRatingsExtendedResponse = List<TraktTvShowRatingExtendedBody>

@Serializable
@SerialName(TraktScreenplayType.TvShow)
data class TraktTvShowRatingExtendedBody(

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowExtendedBody,

    @SerialName(Rating)
    override val rating: Int

) : TraktScreenplayRatingExtendedBody {

    override val screenplay: TraktTvShowExtendedBody
        get() = tvShow

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = tvShow.ids.tmdb
}
