package cinescout.tvshows.data.remote.tmdb.mapper

import arrow.core.Option
import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.model.Person
import cinescout.common.model.TmdbProfileImage
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowCredits
import cinescout.tvshows.domain.model.TvShowCredits

class TmdbTvShowCreditsMapper {

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
