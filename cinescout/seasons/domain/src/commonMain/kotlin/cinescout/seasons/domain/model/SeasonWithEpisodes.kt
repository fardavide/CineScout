package cinescout.seasons.domain.model

import arrow.core.Nel

data class SeasonWithEpisodes(
    val episodes: Nel<Episode>,
    val season: Season
)
