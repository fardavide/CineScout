package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsRatingsMetadataResponse = List<TraktTvShowRatingMetadataBody>

@Serializable
data class TraktTvShowRatingMetadataBody(

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody,

    @SerialName(Rating)
    override val rating: Int
) : TraktScreenplayRatingMetadataBody {

    override val tmdbId: TmdbScreenplayId
        get() = tvShow.ids.tmdb
}
