package cinescout.tvshows.domain.sample

import arrow.core.some
import cinescout.tvshows.domain.model.TvShowWithExtras
import cinescout.tvshows.domain.testdata.TvShowKeywordsTestData

object TvShowWithExtrasSample {

    val BreakingBad = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsSample.BreakingBad,
        credits = TvShowCreditsSample.BreakingBad,
        isInWatchlist = false,
        keywords = TvShowKeywordsTestData.BreakingBad,
        personalRating = TvShowWithPersonalRatingSample.BreakingBad.personalRating.some()
    )

    val Dexter = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsSample.Dexter,
        credits = TvShowCreditsSample.Dexter,
        isInWatchlist = false,
        keywords = TvShowKeywordsTestData.Dexter,
        personalRating = TvShowWithPersonalRatingSample.Dexter.personalRating.some()
    )

    val Grimm = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsSample.Grimm,
        credits = TvShowCreditsSample.Grimm,
        isInWatchlist = true,
        keywords = TvShowKeywordsTestData.Grimm,
        personalRating = TvShowWithPersonalRatingSample.Grimm.personalRating.some()
    )
}
