package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsRatingsMetadataResponse = List<TraktTvShowRatingMetadataBody>

@Serializable
@SerialName(TraktScreenplayType.TvShow)
data class TraktTvShowRatingMetadataBody(

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody,

    @SerialName(Rating)
    override val rating: Int
) : TraktScreenplayRatingMetadataBody {

    override val tmdbId: TmdbScreenplayId
        get() = tvShow.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = tvShow.ids.trakt
}
