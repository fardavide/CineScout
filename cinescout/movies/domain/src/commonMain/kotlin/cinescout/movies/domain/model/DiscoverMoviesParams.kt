package cinescout.movies.domain.model

import arrow.core.Option
import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.model.Genre
import cinescout.common.model.Keyword

data class DiscoverMoviesParams(
    val castMember: Option<CastMember>,
    val crewMember: Option<CrewMember>,
    val genre: Option<Genre>,
    val keyword: Option<Keyword>,
    val releaseYear: Option<ReleaseYear>
)
