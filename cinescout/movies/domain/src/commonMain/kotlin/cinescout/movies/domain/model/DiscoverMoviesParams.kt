package cinescout.movies.domain.model

import arrow.core.Option
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Keyword

data class DiscoverMoviesParams(
    val castMember: Option<CastMember>,
    val crewMember: Option<CrewMember>,
    val genre: Option<Genre>,
    val keyword: Option<Keyword>,
    val releaseYear: Option<ReleaseYear>
)
