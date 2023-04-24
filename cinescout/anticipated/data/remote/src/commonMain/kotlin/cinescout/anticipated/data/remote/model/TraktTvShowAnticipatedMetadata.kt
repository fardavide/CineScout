package cinescout.anticipated.data.remote.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

typealias TraktTvShowsAnticipatedMetadataResponse = List<TraktTvShowAnticipatedMetadataBody>

@Serializable
data class TraktTvShowAnticipatedMetadataBody(

    @SerialName(TraktScreenplayType.TvShow)
    val tvShow: TraktTvShowMetadataBody

) : TraktScreenplayAnticipatedMetadataBody {

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = tvShow.ids.tmdb

    override val traktId: TraktScreenplayId
        get() = tvShow.ids.trakt
}
