package cinescout.tvshows.domain.testdata

import cinescout.common.testdata.GenreTestData
import cinescout.tvshows.domain.model.TvShowWithDetails

object TvShowWithDetailsTestData {

    val BreakingBad = TvShowWithDetails(
        tvShow = TvShowTestData.BreakingBad,
        genres = listOf(GenreTestData.Drama)
    )

    val Dexter = TvShowWithDetails(
        tvShow = TvShowTestData.Dexter,
        genres = listOf(GenreTestData.Crime, GenreTestData.Drama, GenreTestData.Mystery)
    )

    val Grimm = TvShowWithDetails(
        tvShow = TvShowTestData.Grimm,
        genres = listOf(GenreTestData.Drama, GenreTestData.Mystery, GenreTestData.SciFiFantasy)
    )
}
