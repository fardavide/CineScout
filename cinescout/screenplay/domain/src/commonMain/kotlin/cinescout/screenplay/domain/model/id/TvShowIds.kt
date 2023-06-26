package cinescout.screenplay.domain.model.id

import kotlinx.serialization.Serializable

@Serializable
data class TvShowIds(
    override val tmdb: TmdbTvShowId,
    override val trakt: TraktTvShowId
) : ScreenplayIds

@JvmInline
@Serializable
value class TmdbTvShowId(override val value: Int) : TmdbScreenplayId

@JvmInline
@Serializable
value class TraktTvShowId(override val value: Int) : TraktScreenplayId
