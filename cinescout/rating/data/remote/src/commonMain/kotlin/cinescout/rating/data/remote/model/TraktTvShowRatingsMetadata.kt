package cinescout.rating.data.remote.model

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsRatingsMetadataResponse = List<TraktTvShowRatingMetadataBody>

@Serializable
@SerialName(TraktContentType.TvShow)
data class TraktTvShowRatingMetadataBody(
    @SerialName(TraktContentType.TvShow)
    val tvShow: TraktTvShowMetadataBody,
    @SerialName(Rating)
    override val rating: Int
) : TraktScreenplayRatingMetadataBody {

    override val tmdbId: TmdbScreenplayId
        get() = tvShow.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = tvShow.ids.trakt
}
