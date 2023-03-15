package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias TraktTvShowsMetadataResponse = List<TraktTvShowMetadataBody>

@Serializable
data class TraktTvShowMetadataBody(

    @SerialName(TraktScreenplay.Ids)
    val ids: TraktTvShowIds

) : TraktScreenplayMetadataBody {

    override val tmdbId: TmdbScreenplayId.TvShow
        get() = ids.tmdb
}
