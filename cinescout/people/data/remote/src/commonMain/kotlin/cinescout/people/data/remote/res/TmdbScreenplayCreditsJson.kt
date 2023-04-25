package cinescout.people.data.remote.res

import cinescout.people.domain.sample.PersonSample
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.people.data.remote.model.GetScreenplayCreditsResponse as Response

object TmdbScreenplayCreditsJson {

    val Avatar3 = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Avatar3.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.Avatar3.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.SamWorthington.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.SamWorthington.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.SamWorthington.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.Avatar3.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.ZoeSaldana.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.ZoeSaldana.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.ZoeSaldana.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.StephenRivkin.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.Avatar3.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.StephenRivkin.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.StephenRivkin.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val BreakingBad = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.BreakingBad.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.BreakingBad.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.BryanCranston.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.BryanCranston.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.BryanCranston.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.BreakingBad.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.AaronPaul.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.AaronPaul.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.AaronPaul.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.VinceGilligan.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.BreakingBad.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.VinceGilligan.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.VinceGilligan.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val Dexter = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Dexter.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.Dexter.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.MichaelCHall.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.MichaelCHall.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.MichaelCHall.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.Dexter.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.JenniferCarpenter.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.JenniferCarpenter.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.JenniferCarpenter.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.MichaelCHall.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.Dexter.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.MichaelCHall.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.MichaelCHall.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val Grimm = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Grimm.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.Grimm.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.DavidGiuntoli.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.DavidGiuntoli.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.DavidGiuntoli.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.Grimm.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.SilasWeirMitchell.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.SilasWeirMitchell.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.SilasWeirMitchell.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.RussellHornsby.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.Grimm.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.RussellHornsby.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.RussellHornsby.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val Inception = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Inception.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.Inception.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.LeonardoDiCaprio.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.LeonardoDiCaprio.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.LeonardoDiCaprio.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.Inception.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.JosephGordonLevitt.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.JosephGordonLevitt.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.JosephGordonLevitt.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.ChristopherNolan.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.Inception.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.ChristopherNolan.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.ChristopherNolan.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val TheWalkingDeadDeadCity = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.TheWalkingDeadDeadCity.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.TheWalkingDeadDeadCity.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.LaurenCohan.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.LaurenCohan.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.LaurenCohan.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": 
                        "${ScreenplayCreditsSample.TheWalkingDeadDeadCity.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.JeffreyDeanMorgan.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.JeffreyDeanMorgan.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.JeffreyDeanMorgan.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.EliJorne.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.TheWalkingDeadDeadCity.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.EliJorne.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.EliJorne.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.TheWolfOfWallStreet.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.LeonardoDiCaprio.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.LeonardoDiCaprio.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.LeonardoDiCaprio.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.TheWolfOfWallStreet.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.JonahHill.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.JonahHill.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.JonahHill.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.MartinScorsese.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.TheWolfOfWallStreet.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.MartinScorsese.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.MartinScorsese.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.War.value}",
            "${Response.Cast}": [
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.War.cast[0].character.orNull()}",
                    "${Response.Id}": "${PersonSample.HrithikRoshan.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.HrithikRoshan.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.HrithikRoshan.profileImage.orNull()?.path}"
                },
                {
                    "${Response.CastMember.Character}": "${ScreenplayCreditsSample.War.cast[1].character.orNull()}",
                    "${Response.Id}": "${PersonSample.TigerShroff.tmdbId.value}",
                    "${Response.Name}": "${PersonSample.TigerShroff.name}",
                    "${Response.Order}": 1,
                    "${Response.ProfilePath}": "${PersonSample.TigerShroff.profileImage.orNull()?.path}"
                }
            ],
            "${Response.Crew}": [
                {
                    "${Response.Id}": "${PersonSample.SimoneBar.tmdbId.value}",
                    "${Response.CrewMember.Job}": "${ScreenplayCreditsSample.War.crew[0].job.orNull()}",
                    "${Response.Name}": "${PersonSample.SimoneBar.name}",
                    "${Response.Order}": 0,
                    "${Response.ProfilePath}": "${PersonSample.SimoneBar.profileImage.orNull()?.path}"
                }
            ]
        }
    """.trimIndent()
}
