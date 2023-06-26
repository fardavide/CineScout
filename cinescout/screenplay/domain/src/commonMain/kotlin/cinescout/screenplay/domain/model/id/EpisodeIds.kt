package cinescout.screenplay.domain.model.id

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeIds(
    override val tmdb: TmdbEpisodeId,
    override val trakt: TraktEpisodeId
) : ContentIds

@JvmInline
@Serializable
value class TmdbEpisodeId(override val value: Int) : TmdbContentId

@JvmInline
@Serializable
value class TraktEpisodeId(override val value: Int) : TraktContentId
