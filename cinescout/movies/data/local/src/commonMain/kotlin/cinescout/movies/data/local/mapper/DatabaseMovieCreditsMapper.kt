package cinescout.movies.data.local.mapper

import arrow.core.Option
import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.model.Person
import cinescout.common.model.TmdbProfileImage
import cinescout.database.FindCastByMovieId
import cinescout.database.FindCrewByMovieId
import cinescout.movies.domain.model.MovieCredits
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseMovieCreditsMapper {

    fun toCredits(
        movieId: TmdbMovieId,
        cast: List<FindCastByMovieId>,
        crew: List<FindCrewByMovieId>
    ) = MovieCredits(
        movieId = movieId,
        cast = cast.map(::toCastMember),
        crew = crew.map(::toCrewMember)
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
