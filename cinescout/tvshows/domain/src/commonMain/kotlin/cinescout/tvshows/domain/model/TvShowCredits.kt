package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember

data class TvShowCredits(
    val tvShowId: TmdbTvShowId,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)
