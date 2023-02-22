package cinescout.tvshows.domain.testdata

import arrow.core.some
import cinescout.tvshows.domain.model.TvShowWithExtras
import cinescout.tvshows.domain.sample.TvShowWithDetailsSample
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample

object TvShowWithExtrasTestData {

    val BreakingBad = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsSample.BreakingBad,
        credits = TvShowCreditsTestData.BreakingBad,
        isInWatchlist = false,
        keywords = TvShowKeywordsTestData.BreakingBad,
        personalRating = TvShowWithPersonalRatingSample.BreakingBad.personalRating.some()
    )

    val Dexter = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsSample.Dexter,
        credits = TvShowCreditsTestData.Dexter,
        isInWatchlist = false,
        keywords = TvShowKeywordsTestData.Dexter,
        personalRating = TvShowWithPersonalRatingSample.Dexter.personalRating.some()
    )

    val Grimm = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsSample.Grimm,
        credits = TvShowCreditsTestData.Grimm,
        isInWatchlist = true,
        keywords = TvShowKeywordsTestData.Grimm,
        personalRating = TvShowWithPersonalRatingSample.Grimm.personalRating.some()
    )
}
