package cinescout.tvshows.domain.testdata

import cinescout.common.testdata.GenreTestData
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.sample.TvShowSample

object TvShowWithDetailsTestData {

    val BreakingBad = TvShowWithDetails(
        tvShow = TvShowSample.BreakingBad,
        genres = listOf(GenreTestData.Drama)
    )

    val Dexter = TvShowWithDetails(
        tvShow = TvShowSample.Dexter,
        genres = listOf(GenreTestData.Crime, GenreTestData.Drama, GenreTestData.Mystery)
    )

    val Grimm = TvShowWithDetails(
        tvShow = TvShowSample.Grimm,
        genres = listOf(GenreTestData.Drama, GenreTestData.Mystery, GenreTestData.SciFiFantasy)
    )
}
