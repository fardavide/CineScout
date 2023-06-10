package cinescout.screenplay.domain.model

import arrow.core.Nel
import cinescout.screenplay.domain.model.ids.TvShowIds

data class TvShowSeasonsWithEpisodes(
    val tvShowIds: TvShowIds,
    val seasonsWithEpisodes: Nel<SeasonWithEpisodes>
)
