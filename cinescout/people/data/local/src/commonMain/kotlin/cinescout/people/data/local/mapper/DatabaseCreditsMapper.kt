package cinescout.people.data.local.mapper

import arrow.core.toOption
import cinescout.database.FindCastByScreenplayId
import cinescout.database.FindCrewByScreenplayId
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.Person
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseCreditsMapper {

    fun toCredits(
        databaseCast: List<FindCastByScreenplayId>,
        databaseCrew: List<FindCrewByScreenplayId>,
        screenplayId: TmdbScreenplayId
    ) = ScreenplayCredits(
        cast = databaseCast.map(::toCastMember),
        crew = databaseCrew.map(::toCrewMember),
        screenplayId = screenplayId
    )

    private fun toCastMember(databaseCast: FindCastByScreenplayId) = CastMember(
        character = databaseCast.character.toOption(),
        order = databaseCast.memberOrder.toInt(),
        person = Person(
            name = databaseCast.name,
            profileImage = databaseCast.profileImagePath.toOption().map(::TmdbProfileImage),
            tmdbId = databaseCast.personId.toId()
        )
    )

    private fun toCrewMember(databaseCrew: FindCrewByScreenplayId) = CrewMember(
        job = databaseCrew.job.toOption(),
        order = databaseCrew.memberOrder.toInt(),
        person = Person(
            name = databaseCrew.name,
            profileImage = databaseCrew.profileImagePath.toOption().map(::TmdbProfileImage),
            tmdbId = databaseCrew.personId.toId()
        )
    )
}
