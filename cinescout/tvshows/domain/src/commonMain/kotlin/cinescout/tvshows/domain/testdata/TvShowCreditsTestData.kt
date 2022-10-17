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
