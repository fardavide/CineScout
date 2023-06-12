package cinescout.screenplay.domain.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class EpisodeNumber(val value: Int)

data class SeasonAndEpisodeNumber(
    val season: SeasonNumber,
    val episode: EpisodeNumber
)
