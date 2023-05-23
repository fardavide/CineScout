package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMovieIds(

    @SerialName(TraktScreenplayIds.Tmdb)
    override val tmdb: TmdbScreenplayId.Movie,

    @SerialName(TraktScreenplayIds.Trakt)
    override val trakt: TraktScreenplayId.Movie
    
) : TraktScreenplayIds

/**
 * Same as [TraktMovieIds] but with optional fields.
 */
@Serializable
data class OptTraktMovieIds(

    @SerialName(TraktScreenplayIds.Tmdb)
    val tmdb: TmdbScreenplayId.Movie? = null,

    @SerialName(TraktScreenplayIds.Trakt)
    val trakt: TraktScreenplayId.Movie? = null
)
