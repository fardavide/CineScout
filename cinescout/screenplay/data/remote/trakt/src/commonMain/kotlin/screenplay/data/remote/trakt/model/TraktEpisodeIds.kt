package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.id.TmdbContentId
import cinescout.screenplay.domain.model.id.TmdbEpisodeId
import cinescout.screenplay.domain.model.id.TraktEpisodeId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktEpisodeIds(

    @SerialName(TraktContentIds.Tmdb)
    override val tmdb: TmdbEpisodeId = TmdbContentId.invalid(),

    @SerialName(TraktContentIds.Trakt)
    override val trakt: TraktEpisodeId

) : TraktContentIds

/**
 * Same as [TraktEpisodeIds] but with optional fields.
 */
@Serializable
data class OptTraktEpisodeIds(

    @SerialName(TraktContentIds.Tmdb)
    val tmdb: TmdbEpisodeId? = null,

    @SerialName(TraktContentIds.Trakt)
    val trakt: TraktEpisodeId? = null
)
