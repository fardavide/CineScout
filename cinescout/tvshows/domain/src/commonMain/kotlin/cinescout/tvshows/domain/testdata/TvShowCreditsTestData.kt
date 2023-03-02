package cinescout.tvshows.domain.testdata

import arrow.core.some
import cinescout.screenplay.domain.model.CastMember
import cinescout.screenplay.domain.model.CrewMember
import cinescout.screenplay.domain.sample.PersonSample
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample

object TvShowCreditsTestData {

    val BreakingBad = TvShowCredits(
        tvShowId = TmdbTvShowIdSample.BreakingBad,
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
        )
    )

    val Dexter = TvShowCredits(
        tvShowId = TmdbTvShowIdSample.Dexter,
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
        )
    )

    val Grimm = TvShowCredits(
        tvShowId = TmdbTvShowIdSample.Grimm,
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
        )
    )
}
