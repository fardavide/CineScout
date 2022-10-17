package cinescout.tvshows.domain.model

import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember

data class TvShowCredits(
    val tvShowId: TmdbTvShowId,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)
