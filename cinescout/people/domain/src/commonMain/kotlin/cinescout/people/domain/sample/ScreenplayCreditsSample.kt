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
                person = PersonSample.BryanCranston
            ),
            CastMember(
                character = "Jesse Pinkman".some(),
                person = PersonSample.AaronPaul
            ),
            CastMember(
                character = "Skyler White".some(),
                person = PersonSample.AnnaGunn
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                person = PersonSample.VinceGilligan
            )
        ),
        screenplayId = TmdbScreenplayIdSample.BreakingBad
    )

    val Dexter = TvShowCredits(
        cast = listOf(
            CastMember(
                character = "Dexter Morgan".some(),
                person = PersonSample.MichaelCHall
            ),
            CastMember(
                character = "Debra Morgan".some(),
                person = PersonSample.JenniferCarpenter
            ),
            CastMember(
                character = "Angel Batista".some(),
                person = PersonSample.DavidZayas
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                person = PersonSample.MichaelCHall
            )
        ),
        screenplayId = TmdbScreenplayIdSample.Dexter
    )

    val Grimm = TvShowCredits(
        cast = listOf(
            CastMember(
                character = "Nick Burkhardt".some(),
                person = PersonSample.DavidGiuntoli
            ),
            CastMember(
                character = "Monroe".some(),
                person = PersonSample.SilasWeirMitchell
            ),
            CastMember(
                character = "Hank Griffin".some(),
                person = PersonSample.RussellHornsby
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                person = PersonSample.DavidGreenwalt
            )
        ),
        screenplayId = TmdbScreenplayIdSample.Grimm
    )

    val Inception = MovieCredits(
        cast = listOf(
            CastMember(
                character = "Dom Cobb".some(),
                person = PersonSample.LeonardoDiCaprio
            ),
            CastMember(
                character = "Arthur".some(),
                person = PersonSample.JosephGordonLevitt
            ),
            CastMember(
                character = "Saito".some(),
                person = PersonSample.KenWatanabe
            ),
            CastMember(
                character = "Eames".some(),
                person = PersonSample.TomHardy
            ),
            CastMember(
                character = "Ariadne".some(),
                person = PersonSample.ElliotPage
            ),
            CastMember(
                character = "Yusuf".some(),
                person = PersonSample.DileepRao
            ),
            CastMember(
                character = "Robert Fischer, Jr.".some(),
                person = PersonSample.CillianMurphy
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                person = PersonSample.ChristopherNolan
            )
        ),
        screenplayId = TmdbScreenplayIdSample.Inception
    )

    val TheWolfOfWallStreet = MovieCredits(
        cast = listOf(
            CastMember(
                character = "Jordan Belfort".some(),
                person = PersonSample.LeonardoDiCaprio
            ),
            CastMember(
                character = "Donnie Azoff".some(),
                person = PersonSample.JonahHill
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Director".some(),
                person = PersonSample.MartinScorsese
            )
        ),
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )

    val War = MovieCredits(
        cast = listOf(
            CastMember(
                character = "Major Kabir Dhaliwal".some(),
                person = PersonSample.HrithikRoshan
            ),
            CastMember(
                character = "Captain Khalid Rahmani".some(),
                person = PersonSample.TigerShroff
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Casting Director".some(),
                person = PersonSample.SimoneBar
            )
        ),
        screenplayId = TmdbScreenplayIdSample.War
    )
}
