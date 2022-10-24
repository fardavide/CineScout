package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.common.testdata.PersonTestData
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowCredits.Response
import cinescout.tvshows.domain.testdata.TvShowCreditsTestData

object TmdbTvShowCreditsJson {

    val BreakingBad = """
        {
            "${Response.Id}": "${TvShowCreditsTestData.BreakingBad.tvShowId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${TvShowCreditsTestData.BreakingBad.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.BryanCranston.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.BryanCranston.name}",
                    "${Response.ProfilePath}": "${PersonTestData.BryanCranston.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${TvShowCreditsTestData.BreakingBad.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.AaronPaul.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.AaronPaul.name}",
                    "${Response.ProfilePath}": "${PersonTestData.AaronPaul.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonTestData.VinceGilligan.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${TvShowCreditsTestData.BreakingBad.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonTestData.VinceGilligan.name}",
                    "${Response.ProfilePath}": "${PersonTestData.VinceGilligan.profileImage.orNull()?.path}"
                }
            ]
        }
    """

    val Dexter = """
        {
            "${Response.Id}": "${TvShowCreditsTestData.Dexter.tvShowId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${TvShowCreditsTestData.Dexter.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.MichaelCHall.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.MichaelCHall.name}",
                    "${Response.ProfilePath}": "${PersonTestData.MichaelCHall.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${TvShowCreditsTestData.Dexter.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonTestData.JenniferCarpenter.tmdbId.value}",
                    "${Response.Name}": "${PersonTestData.JenniferCarpenter.name}",
                    "${Response.ProfilePath}": "${PersonTestData.JenniferCarpenter.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonTestData.MichaelCHall.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${TvShowCreditsTestData.Dexter.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonTestData.MichaelCHall.name}",
                    "${Response.ProfilePath}": "${PersonTestData.MichaelCHall.profileImage.orNull()?.path}"
                }
            ]
        }
    """

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
