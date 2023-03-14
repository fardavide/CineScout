package cinescout.people.data.local.mapper

import arrow.core.Option
import cinescout.database.FindCastByMovieId
import cinescout.database.FindCrewByMovieId
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.Person
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseCreditsMapper {

    fun toCredits(
        screenplayId: TmdbScreenplayId,
        cast: List<FindCastByMovieId>,
        crew: List<FindCrewByMovieId>
    ) = ScreenplayCredits(
        cast = cast.map(::toCastMember),
        crew = crew.map(::toCrewMember),
        screenplayId = screenplayId
    )

    private fun toCastMember(member: FindCastByMovieId) = CastMember(
        character = Option.fromNullable(member.character),
        person = toPerson(member)
    )

    private fun toCrewMember(member: FindCrewByMovieId) = CrewMember(
        job = Option.fromNullable(member.job),
        person = toPerson(member)
    )

    private fun toPerson(member: FindCastByMovieId) = Person(
        tmdbId = member.personId.toId(),
        name = member.name,
        profileImage = Option.fromNullable(member.profileImagePath).map(::TmdbProfileImage)
    )

    private fun toPerson(member: FindCrewByMovieId) = Person(
        tmdbId = member.personId.toId(),
        name = member.name,
        profileImage = Option.fromNullable(member.profileImagePath).map(::TmdbProfileImage)
    )
}
