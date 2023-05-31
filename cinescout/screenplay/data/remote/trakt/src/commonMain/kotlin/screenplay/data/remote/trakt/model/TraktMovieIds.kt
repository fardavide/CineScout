package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TraktMovieId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMovieIds(
    @SerialName(TraktContentIds.Tmdb)
    override val tmdb: TmdbMovieId,
    @SerialName(TraktContentIds.Trakt)
    override val trakt: TraktMovieId
) : TraktScreenplayIds

/**
 * Same as [TraktMovieIds] but with optional fields.
 */
@Serializable
data class OptTraktMovieIds(
    @SerialName(TraktContentIds.Tmdb)
    val tmdb: TmdbMovieId? = null,
    @SerialName(TraktContentIds.Trakt)
    val trakt: TraktMovieId? = null
)
