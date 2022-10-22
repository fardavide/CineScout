package cinescout.tvshows.domain.testdata

import cinescout.common.testdata.KeywordTestData
import cinescout.tvshows.domain.model.TvShowKeywords

object TvShowKeywordsTestData {

    val BreakingBad = TvShowKeywords(
        tvShowId = TmdbTvShowIdTestData.BreakingBad,
        keywords = listOf(
            KeywordTestData.DrugDealer,
            KeywordTestData.Psychopath,
            KeywordTestData.NewMexico
        )
    )

    val Dexter = TvShowKeywords(
        tvShowId = TmdbTvShowIdTestData.Dexter,
        keywords = listOf(
            KeywordTestData.DoubleLife,
            KeywordTestData.BasedOnNovelOrBook,
            KeywordTestData.MiamiFlorida
        )
    )

    val Grimm = TvShowKeywords(
        tvShowId = TmdbTvShowIdTestData.Grimm,
        keywords = listOf(
            KeywordTestData.Police,
            KeywordTestData.FairyTale,
            KeywordTestData.Detective
        )
    )
}
