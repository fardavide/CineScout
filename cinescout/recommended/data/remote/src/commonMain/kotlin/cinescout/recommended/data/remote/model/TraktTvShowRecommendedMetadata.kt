package cinescout.recommended.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsRecommendedMetadataResponse = List<TraktTvShowRecommendedMetadataBody>

@Serializable
data class TraktTvShowRecommendedMetadataBody(

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody

) : TraktScreenplayRecommendedMetadataBody {

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = tvShow.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = tvShow.ids.trakt
}