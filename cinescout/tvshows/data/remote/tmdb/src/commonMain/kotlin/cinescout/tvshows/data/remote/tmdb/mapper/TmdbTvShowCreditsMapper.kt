package cinescout.tvshows.data.remote.tmdb.mapper

import arrow.core.Option
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.model.Person
import cinescout.screenplay.domain.model.TmdbProfileImage
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowCredits
import cinescout.tvshows.domain.model.TvShowCredits
import org.koin.core.annotation.Factory

@Factory
internal class TmdbTvShowCreditsMapper {

    fun toTvShowCredits(credits: GetTvShowCredits.Response) = TvShowCredits(
        tvShowId = credits.tvShowId,
        cast = credits.cast.map(::toCastMember),
        crew = credits.crew.map(::toCrewMember)
    )

    private fun toCastMember(member: GetTvShowCredits.Response.CastMember) = CastMember(
        character = Option.fromNullable(member.character),
        person = toPerson(member)
    )

    private fun toCrewMember(member: GetTvShowCredits.Response.CrewMember) = CrewMember(
        job = Option.fromNullable(member.job),
        person = toPerson(member)
    )

    private fun toPerson(member: GetTvShowCredits.Response.CastMember) = Person(
        name = member.name,
        profileImage = Option.fromNullable(member.profilePath).map(::TmdbProfileImage),
        tmdbId = member.id
    )

    private fun toPerson(member: GetTvShowCredits.Response.CrewMember) = Person(
        name = member.name,
        profileImage = Option.fromNullable(member.profilePath).map(::TmdbProfileImage),
        tmdbId = member.id
    )
}
