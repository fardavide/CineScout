package cinescout.tvshows.domain.sample

import cinescout.screenplay.domain.sample.GenreSample
import cinescout.tvshows.domain.model.TvShowWithDetails

object TvShowWithDetailsSample {

    val BreakingBad = TvShowWithDetails(
        tvShow = TvShowSample.BreakingBad,
        genres = listOf(GenreSample.Drama)
    )

    val Dexter = TvShowWithDetails(
        tvShow = TvShowSample.Dexter,
        genres = listOf(GenreSample.Crime, GenreSample.Drama, GenreSample.Mystery)
    )

    val Grimm = TvShowWithDetails(
        tvShow = TvShowSample.Grimm,
        genres = listOf(GenreSample.Drama, GenreSample.Mystery, GenreSample.SciFiFantasy)
    )
}
