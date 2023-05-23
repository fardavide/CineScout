package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktTvShowIds(

    @SerialName(TraktScreenplayIds.Tmdb)
    override val tmdb: TmdbScreenplayId.TvShow = TmdbScreenplayId.invalid(),

    @SerialName(TraktScreenplayIds.Trakt)
    override val trakt: TraktScreenplayId.TvShow

) : TraktScreenplayIds

/**
 * Same as [TraktTvShowIds] but with optional fields.
 */
@Serializable
data class OptTraktTvShowIds(

    @SerialName(TraktScreenplayIds.Tmdb)
    val tmdb: TmdbScreenplayId.TvShow? = null,

    @SerialName(TraktScreenplayIds.Trakt)
    val trakt: TraktScreenplayId.TvShow? = null
)
