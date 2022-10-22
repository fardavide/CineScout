package cinescout.tvshows.domain.testdata

import arrow.core.some
import cinescout.tvshows.domain.model.TvShowWithExtras

object TvShowWithExtrasTestData {

    val BreakingBad = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsTestData.BreakingBad,
        credits = TvShowCreditsTestData.BreakingBad,
        isInWatchlist = false,
        keywords = TvShowKeywordsTestData.BreakingBad,
        personalRating = TvShowWithPersonalRatingTestData.BreakingBad.personalRating.some()
    )

    val Dexter = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsTestData.Dexter,
        credits = TvShowCreditsTestData.Dexter,
        isInWatchlist = false,
        keywords = TvShowKeywordsTestData.Dexter,
        personalRating = TvShowWithPersonalRatingTestData.Dexter.personalRating.some()
    )

    val Grimm = TvShowWithExtras(
        tvShowWithDetails = TvShowWithDetailsTestData.Grimm,
        credits = TvShowCreditsTestData.Grimm,
        isInWatchlist = true,
        keywords = TvShowKeywordsTestData.Grimm,
        personalRating = TvShowWithPersonalRatingTestData.Grimm.personalRating.some()
    )
}
