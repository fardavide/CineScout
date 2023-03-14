package cinescout.people.domain.model

import arrow.core.Option

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
