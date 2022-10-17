package cinescout.movies.domain.model

import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember

data class MovieCredits(
    val movieId: TmdbMovieId,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)
