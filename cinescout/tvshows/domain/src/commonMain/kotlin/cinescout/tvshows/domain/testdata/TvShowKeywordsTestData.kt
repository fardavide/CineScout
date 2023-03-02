package cinescout.tvshows.domain.testdata

import cinescout.screenplay.domain.sample.KeywordSample
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample

object TvShowKeywordsTestData {

    val BreakingBad = TvShowKeywords(
        tvShowId = TmdbTvShowIdSample.BreakingBad,
        keywords = listOf(
            KeywordSample.DrugDealer,
            KeywordSample.Psychopath,
            KeywordSample.NewMexico
        )
    )

    val Dexter = TvShowKeywords(
        tvShowId = TmdbTvShowIdSample.Dexter,
        keywords = listOf(
            KeywordSample.DoubleLife,
            KeywordSample.BasedOnNovelOrBook,
            KeywordSample.MiamiFlorida
        )
    )

    val Grimm = TvShowKeywords(
        tvShowId = TmdbTvShowIdSample.Grimm,
        keywords = listOf(
            KeywordSample.Police,
            KeywordSample.FairyTale,
            KeywordSample.Detective
        )
    )
}
