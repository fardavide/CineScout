package cinescout.tvshows.domain.testdata

import cinescout.common.testdata.KeywordTestData
import cinescout.tvshows.domain.model.TvShowKeywords

object TvShowKeywordsTestData {

    val Grimm = TvShowKeywords(
        tvShowId = TmdbTvShowIdTestData.Grimm,
        keywords = listOf(
            KeywordTestData.Police,
            KeywordTestData.FairyTale,
            KeywordTestData.Detective
        )
    )
}
