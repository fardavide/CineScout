package cinescout.seasons.domain.model

import arrow.core.Nel
import cinescout.screenplay.domain.model.id.TvShowIds

data class TvShowSeasonsWithEpisodes(
    val tvShowIds: TvShowIds,
    val seasonsWithEpisodes: Nel<SeasonWithEpisodes>
)
