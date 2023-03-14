package cinescout.movies.data.remote.tmdb.mapper

import arrow.core.Option
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.movies.data.remote.tmdb.model.GetMovieCredits
import cinescout.movies.domain.model.MovieCredits
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.model.Person
import org.koin.core.annotation.Factory

@Factory
internal class TmdbMovieCreditsMapper {

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
