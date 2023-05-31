package screenplay.data.remote.trakt.model

import cinescout.screenplay.domain.model.ids.TmdbContentId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import cinescout.screenplay.domain.model.ids.TraktTvShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktTvShowIds(
    @SerialName(TraktContentIds.Tmdb)
    override val tmdb: TmdbTvShowId = TmdbContentId.invalid(),
    @SerialName(TraktContentIds.Trakt)
    override val trakt: TraktTvShowId
) : TraktScreenplayIds

/**
 * Same as [TraktTvShowIds] but with optional fields.
 */
@Serializable
data class OptTraktTvShowIds(
    @SerialName(TraktContentIds.Tmdb)
    val tmdb: TmdbTvShowId? = null,
    @SerialName(TraktContentIds.Trakt)
    val trakt: TraktTvShowId? = null
)
