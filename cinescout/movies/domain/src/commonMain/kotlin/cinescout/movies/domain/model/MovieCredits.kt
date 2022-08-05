package cinescout.movies.domain.model

data class MovieCredits(
    val movieId: TmdbMovieId,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
) {

    sealed interface CreditsMember {

        val person: Person
    }

    data class CastMember(
        val character: String,
        override val person: Person
    ) : CreditsMember

    data class CrewMember(
        val job: String,
        override val person: Person
    ) : CreditsMember
}
