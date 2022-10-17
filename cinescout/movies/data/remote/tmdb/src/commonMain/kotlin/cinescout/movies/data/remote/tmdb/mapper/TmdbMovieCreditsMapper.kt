package cinescout.movies.data.remote.tmdb.mapper

import arrow.core.Option
import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.model.Person
import cinescout.common.model.TmdbProfileImage
import cinescout.movies.data.remote.tmdb.model.GetMovieCredits
import cinescout.movies.domain.model.MovieCredits

class TmdbMovieCreditsMapper {

    fun toMovieCredits(credits: GetMovieCredits.Response) = MovieCredits(
        movieId = credits.movieId,
        cast = credits.cast.map(::toCastMember),
        crew = credits.crew.map(::toCrewMember)
    )

    private fun toCastMember(member: GetMovieCredits.Response.CastMember) = CastMember(
        character = Option.fromNullable(member.character),
        person = toPerson(member)
    )

    private fun toCrewMember(member: GetMovieCredits.Response.CrewMember) = CrewMember(
        job = Option.fromNullable(member.job),
        person = toPerson(member)
    )

    private fun toPerson(member: GetMovieCredits.Response.CastMember) = Person(
        name = member.name,
        profileImage = Option.fromNullable(member.profilePath).map(::TmdbProfileImage),
        tmdbId = member.id
    )

    private fun toPerson(member: GetMovieCredits.Response.CrewMember) = Person(
        name = member.name,
        profileImage = Option.fromNullable(member.profilePath).map(::TmdbProfileImage),
        tmdbId = member.id
    )
}
