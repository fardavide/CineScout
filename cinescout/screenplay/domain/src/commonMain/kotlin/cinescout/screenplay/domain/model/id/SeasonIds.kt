package cinescout.screenplay.domain.model.id

import kotlinx.serialization.Serializable

@Serializable
data class SeasonIds(
    override val tmdb: TmdbSeasonId,
    override val trakt: TraktSeasonId
) : ContentIds

@JvmInline
@Serializable
value class TmdbSeasonId(override val value: Int) : TmdbContentId

@JvmInline
@Serializable
value class TraktSeasonId(override val value: Int) : TraktContentId
