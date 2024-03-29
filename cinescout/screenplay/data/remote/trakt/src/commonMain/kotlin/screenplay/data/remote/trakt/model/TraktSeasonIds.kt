package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.id.TmdbContentId
import cinescout.screenplay.domain.model.id.TmdbSeasonId
import cinescout.screenplay.domain.model.id.TraktSeasonId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktSeasonIds(

    @SerialName(TraktContentIds.Tmdb)
    override val tmdb: TmdbSeasonId = TmdbContentId.invalid(),

    @SerialName(TraktContentIds.Trakt)
    override val trakt: TraktSeasonId

) : TraktContentIds

/**
 * Same as [TraktSeasonIds] but with optional fields.
 */
@Serializable
data class OptTraktSeasonIds(

    @SerialName(TraktContentIds.Tmdb)
    val tmdb: TmdbSeasonId? = null,

    @SerialName(TraktContentIds.Trakt)
    val trakt: TraktSeasonId? = null
)
