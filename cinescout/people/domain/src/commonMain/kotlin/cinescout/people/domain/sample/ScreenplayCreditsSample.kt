package cinescout.people.domain.sample

import arrow.core.some
import cinescout.people.domain.model.CastMember
import cinescout.people.domain.model.CrewMember
import cinescout.people.domain.model.MovieCredits
import cinescout.people.domain.model.TvShowCredits
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample

object ScreenplayCreditsSample {

    val BreakingBad = TvShowCredits(
        cast = listOf(
            CastMember(
                character = "Walter White".some(),
                order = 0,
                person = PersonSample.BryanCranston
            ),
            CastMember(
                character = "Jesse Pinkman".some(),
                order = 1,
                person = PersonSample.AaronPaul
            ),
            CastMember(
                character = "Skyler White".some(),
                order = 2,
                person = PersonSample.AnnaGunn
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                order = 0,
                person = PersonSample.VinceGilligan
            )
        ),
        screenplayId = TmdbScreenplayIdSample.BreakingBad
    )

    val Dexter = TvShowCredits(
        cast = listOf(
            CastMember(
                character = "Dexter Morgan".some(),
                order = 0,
                person = PersonSample.MichaelCHall
            ),
            CastMember(
                character = "Debra Morgan".some(),
                order = 1,
                person = PersonSample.JenniferCarpenter
            ),
            CastMember(
                character = "Angel Batista".some(),
                order = 2,
                person = PersonSample.DavidZayas
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                order = 0,
                person = PersonSample.MichaelCHall
            )
        ),
        screenplayId = TmdbScreenplayIdSample.Dexter
    )

    val Grimm = TvShowCredits(
        cast = listOf(
            CastMember(
                character = "Nick Burkhardt".some(),
                order = 0,
                person = PersonSample.DavidGiuntoli
            ),
            CastMember(
                character = "Monroe".some(),
                order = 1,
                person = PersonSample.SilasWeirMitchell
            ),
            CastMember(
                character = "Hank Griffin".some(),
                order = 2,
                person = PersonSample.RussellHornsby
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                order = 0,
                person = PersonSample.DavidGreenwalt
            )
        ),
        screenplayId = TmdbScreenplayIdSample.Grimm
    )

    val Inception = MovieCredits(
        cast = listOf(
            CastMember(
                character = "Dom Cobb".some(),
                order = 0,
                person = PersonSample.LeonardoDiCaprio
            ),
            CastMember(
                character = "Arthur".some(),
                order = 1,
                person = PersonSample.JosephGordonLevitt
            ),
            CastMember(
                character = "Saito".some(),
                order = 2,
                person = PersonSample.KenWatanabe
            ),
            CastMember(
                character = "Eames".some(),
                order = 3,
                person = PersonSample.TomHardy
            ),
            CastMember(
                character = "Ariadne".some(),
                order = 4,
                person = PersonSample.ElliotPage
            ),
            CastMember(
                character = "Yusuf".some(),
                order = 5,
                person = PersonSample.DileepRao
            ),
            CastMember(
                character = "Robert Fischer, Jr.".some(),
                order = 6,
                person = PersonSample.CillianMurphy
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                order = 0,
                person = PersonSample.ChristopherNolan
            )
        ),
        screenplayId = TmdbScreenplayIdSample.Inception
    )

    val TheWolfOfWallStreet = MovieCredits(
        cast = listOf(
            CastMember(
                character = "Jordan Belfort".some(),
                order = 0,
                person = PersonSample.LeonardoDiCaprio
            ),
            CastMember(
                character = "Donnie Azoff".some(),
                order = 1,
                person = PersonSample.JonahHill
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                order = 0,
                person = PersonSample.MartinScorsese
            )
        ),
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )

    val War = MovieCredits(
        cast = listOf(
            CastMember(
                character = "Major Kabir Dhaliwal".some(),
                order = 0,
                person = PersonSample.HrithikRoshan
            ),
            CastMember(
                character = "Captain Khalid Rahmani".some(),
                order = 1,
                person = PersonSample.TigerShroff
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Casting Director".some(),
                order = 0,
                person = PersonSample.SimoneBar
            )
        ),
        screenplayId = TmdbScreenplayIdSample.War
    )
}
