package cinescout.tvshows.domain.testdata

import arrow.core.some
import cinescout.common.model.CastMember
import cinescout.common.model.CrewMember
import cinescout.common.testdata.PersonTestData
import cinescout.tvshows.domain.model.TvShowCredits

object TvShowCreditsTestData {

    val BreakingBad = TvShowCredits(
        tvShowId = TmdbTvShowIdTestData.BreakingBad,
        cast = listOf(
            CastMember(
                character = "Walter White".some(),
                person = PersonTestData.BryanCranston
            ),
            CastMember(
                character = "Jesse Pinkman".some(),
                person = PersonTestData.AaronPaul
            ),
            CastMember(
                character = "Skyler White".some(),
                person = PersonTestData.AnnaGunn
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                person = PersonTestData.VinceGilligan
            )
        )
    )

    val Dexter = TvShowCredits(
        tvShowId = TmdbTvShowIdTestData.Dexter,
        cast = listOf(
            CastMember(
                character = "Dexter Morgan".some(),
                person = PersonTestData.MichaelCHall
            ),
            CastMember(
                character = "Debra Morgan".some(),
                person = PersonTestData.JenniferCarpenter
            ),
            CastMember(
                character = "Angel Batista".some(),
                person = PersonTestData.DavidZayas
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                person = PersonTestData.MichaelCHall
            )
        )
    )

    val Grimm = TvShowCredits(
        tvShowId = TmdbTvShowIdTestData.Grimm,
        cast = listOf(
            CastMember(
                character = "Nick Burkhardt".some(),
                person = PersonTestData.DavidGiuntoli
            ),
            CastMember(
                character = "Monroe".some(),
                person = PersonTestData.SilasWeirMitchell
            ),
            CastMember(
                character = "Hank Griffin".some(),
                person = PersonTestData.RussellHornsby
            )
        ),
        crew = listOf(
            CrewMember(
                job = "Executive Producer".some(),
                person = PersonTestData.DavidGreenwalt
            )
        )
    )
}
