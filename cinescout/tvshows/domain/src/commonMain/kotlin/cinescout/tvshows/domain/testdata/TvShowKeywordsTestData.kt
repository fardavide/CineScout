package cinescout.tvshows.domain.testdata

import cinescout.common.testdata.KeywordTestData
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample

object TvShowKeywordsTestData {

    val BreakingBad = TvShowKeywords(
        tvShowId = TmdbTvShowIdSample.BreakingBad,
        keywords = listOf(
            KeywordTestData.DrugDealer,
            KeywordTestData.Psychopath,
            KeywordTestData.NewMexico
        )
    )

    val Dexter = TvShowKeywords(
        tvShowId = TmdbTvShowIdSample.Dexter,
        keywords = listOf(
            KeywordTestData.DoubleLife,
            KeywordTestData.BasedOnNovelOrBook,
            KeywordTestData.MiamiFlorida
        )
    )

    val Grimm = TvShowKeywords(
        tvShowId = TmdbTvShowIdSample.Grimm,
        keywords = listOf(
            KeywordTestData.Police,
            KeywordTestData.FairyTale,
            KeywordTestData.Detective
        )
    )
}
