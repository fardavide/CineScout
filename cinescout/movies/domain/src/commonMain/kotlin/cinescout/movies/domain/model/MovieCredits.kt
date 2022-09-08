package cinescout.movies.domain.model

import arrow.core.Option

data class MovieCredits(
    val movieId: TmdbMovieId,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
) {

    sealed interface CreditsMember {

        val person: Person
    }

    data class CastMember(
        val character: Option<String>,
        override val person: Person
    ) : CreditsMember

    data class CrewMember(
        val job: Option<String>,
        override val person: Person
    ) : CreditsMember
}
