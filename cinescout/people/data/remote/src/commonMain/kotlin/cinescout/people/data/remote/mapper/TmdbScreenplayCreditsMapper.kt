package cinescout.people.data.remote.mapper

import arrow.core.Option
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.data.remote.model.GetScreenplayCreditsResponse
import cinescout.people.data.remote.model.GetScreenplayCreditsResponseWithId
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.Person
import cinescout.people.domain.model.ScreenplayCredits
import org.koin.core.annotation.Factory

@Factory
internal class TmdbScreenplayCreditsMapper {

    fun toScreenplayCredits(credits: GetScreenplayCreditsResponseWithId) = ScreenplayCredits(
        cast = credits.response.cast.map(::toCastMember),
        crew = credits.response.crew.map(::toCrewMember),
        screenplayId = credits.screenplayId
    )

    private fun toCastMember(member: GetScreenplayCreditsResponse.CastMember) = CastMember(
        character = Option.fromNullable(member.character),
        order = member.order,
        person = toPerson(member)
    )

    private fun toCrewMember(member: GetScreenplayCreditsResponse.CrewMember) = CrewMember(
        job = Option.fromNullable(member.job),
        order = member.order,
        person = toPerson(member)
    )

    private fun toPerson(member: GetScreenplayCreditsResponse.CastMember) = Person(
        name = member.name,
        profileImage = Option.fromNullable(member.profilePath).map(::TmdbProfileImage),
        tmdbId = member.id
    )

    private fun toPerson(member: GetScreenplayCreditsResponse.CrewMember) = Person(
        name = member.name,
        profileImage = Option.fromNullable(member.profilePath).map(::TmdbProfileImage),
        tmdbId = member.id
    )
}
