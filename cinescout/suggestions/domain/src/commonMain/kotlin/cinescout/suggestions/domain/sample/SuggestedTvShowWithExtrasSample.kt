package cinescout.suggestions.domain.sample

import cinescout.suggestions.domain.model.SuggestedTvShowWithExtras
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithExtrasSample

object SuggestedTvShowWithExtrasSample {

    val BreakingBad = SuggestedTvShowWithExtras(
        tvShow = TvShowSample.BreakingBad,
        affinity = SuggestedTvShowSample.BreakingBad.affinity,
        genres = TvShowWithExtrasSample.BreakingBad.tvShowWithDetails.genres,
        credits = TvShowWithExtrasSample.BreakingBad.credits,
        isInWatchlist = TvShowWithExtrasSample.BreakingBad.isInWatchlist,
        keywords = TvShowWithExtrasSample.BreakingBad.keywords,
        personalRating = TvShowWithExtrasSample.BreakingBad.personalRating,
        source = SuggestedTvShowSample.BreakingBad.source
    )

    val Dexter = SuggestedTvShowWithExtras(
        tvShow = TvShowSample.Dexter,
        affinity = SuggestedTvShowSample.Dexter.affinity,
        genres = TvShowWithExtrasSample.Dexter.tvShowWithDetails.genres,
        credits = TvShowWithExtrasSample.Dexter.credits,
        isInWatchlist = TvShowWithExtrasSample.Dexter.isInWatchlist,
        keywords = TvShowWithExtrasSample.Dexter.keywords,
        personalRating = TvShowWithExtrasSample.Dexter.personalRating,
        source = SuggestedTvShowSample.Dexter.source
    )

    val Grimm = SuggestedTvShowWithExtras(
        tvShow = TvShowSample.Grimm,
        affinity = SuggestedTvShowSample.Grimm.affinity,
        genres = TvShowWithExtrasSample.Grimm.tvShowWithDetails.genres,
        credits = TvShowWithExtrasSample.Grimm.credits,
        isInWatchlist = TvShowWithExtrasSample.Grimm.isInWatchlist,
        keywords = TvShowWithExtrasSample.Grimm.keywords,
        personalRating = TvShowWithExtrasSample.Grimm.personalRating,
        source = SuggestedTvShowSample.Grimm.source
    )
}
