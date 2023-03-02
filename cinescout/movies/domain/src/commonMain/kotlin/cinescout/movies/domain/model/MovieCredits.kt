package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember

data class MovieCredits(
    val movieId: TmdbMovieId,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)
