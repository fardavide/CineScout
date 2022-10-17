package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.common.testdata.PersonTestData
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowCredits.Response
import cinescout.tvshows.domain.testdata.TvShowCreditsTestData

object TmdbTvShowCreditsJson {

    val Grimm = """
        {
            "${Response.Id}": "${TvShowCreditsTestData.Grimm.tvShowId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${TvShowCreditsTestData.Grimm.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.DavidGiuntoli.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.DavidGiuntoli.name}",
                    "${Response.ProfilePath}": "${PersonTestData.DavidGiuntoli.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${TvShowCreditsTestData.Grimm.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.SilasWeirMitchell.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.SilasWeirMitchell.name}",
                    "${Response.ProfilePath}": "${PersonTestData.SilasWeirMitchell.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonTestData.RussellHornsby.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${TvShowCreditsTestData.Grimm.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonTestData.RussellHornsby.name}",
                    "${Response.ProfilePath}": "${PersonTestData.RussellHornsby.profileImage.orNull()?.path}"
                }
            ]
        }
    """
}
