package cinescout.tvshows.domain.sample

import cinescout.screenplay.domain.sample.GenreSample
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.tvshows.domain.model.TvShowWithDetails

object TvShowWithDetailsSample {

    val BreakingBad = TvShowWithDetails(
        tvShow = ScreenplaySample.BreakingBad,
        genres = listOf(GenreSample.Drama)
    )

    val Dexter = TvShowWithDetails(
        tvShow = ScreenplaySample.Dexter,
        genres = listOf(GenreSample.Crime, GenreSample.Drama, GenreSample.Mystery)
    )

    val Grimm = TvShowWithDetails(
        tvShow = ScreenplaySample.Grimm,
        genres = listOf(GenreSample.Drama, GenreSample.Mystery, GenreSample.SciFiFantasy)
    )
}
