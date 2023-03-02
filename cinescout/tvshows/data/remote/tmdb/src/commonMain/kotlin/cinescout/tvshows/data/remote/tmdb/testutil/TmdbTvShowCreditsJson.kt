package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.screenplay.domain.sample.PersonSample
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
                    "${Response.Id}": "${PersonSample.BryanCranston.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.BryanCranston.name}",
                    "${Response.ProfilePath}": "${PersonSample.BryanCranston.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${TvShowCreditsTestData.BreakingBad.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.AaronPaul.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.AaronPaul.name}",
                    "${Response.ProfilePath}": "${PersonSample.AaronPaul.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.VinceGilligan.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${TvShowCreditsTestData.BreakingBad.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.VinceGilligan.name}",
                    "${Response.ProfilePath}": "${PersonSample.VinceGilligan.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val Dexter = """
        {
            "${Response.Id}": "${TvShowCreditsTestData.Dexter.tvShowId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${TvShowCreditsTestData.Dexter.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.MichaelCHall.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.MichaelCHall.name}",
                    "${Response.ProfilePath}": "${PersonSample.MichaelCHall.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${TvShowCreditsTestData.Dexter.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.JenniferCarpenter.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.JenniferCarpenter.name}",
                    "${Response.ProfilePath}": "${PersonSample.JenniferCarpenter.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.MichaelCHall.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${TvShowCreditsTestData.Dexter.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.MichaelCHall.name}",
                    "${Response.ProfilePath}": "${PersonSample.MichaelCHall.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val Grimm = """
        {
            "${Response.Id}": "${TvShowCreditsTestData.Grimm.tvShowId.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${TvShowCreditsTestData.Grimm.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.DavidGiuntoli.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.DavidGiuntoli.name}",
                    "${Response.ProfilePath}": "${PersonSample.DavidGiuntoli.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${TvShowCreditsTestData.Grimm.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.SilasWeirMitchell.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.SilasWeirMitchell.name}",
                    "${Response.ProfilePath}": "${PersonSample.SilasWeirMitchell.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.RussellHornsby.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${TvShowCreditsTestData.Grimm.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.RussellHornsby.name}",
                    "${Response.ProfilePath}": "${PersonSample.RussellHornsby.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()
}
