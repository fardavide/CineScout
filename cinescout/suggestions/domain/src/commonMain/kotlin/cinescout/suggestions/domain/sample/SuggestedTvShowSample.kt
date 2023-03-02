package cinescout.suggestions.domain.sample

import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample

object SuggestedTvShowSample {

    val BreakingBad = SuggestedTvShow(
        tvShow = TvShowSample.BreakingBad,
        source = SuggestionSource.FromRated(TvShowWithPersonalRatingSample.BreakingBad.personalRating)
    )

    val Dexter = SuggestedTvShow(
        tvShow = TvShowSample.Dexter,
        source = SuggestionSource.FromRated(TvShowWithPersonalRatingSample.Dexter.personalRating)
    )

    val Grimm = SuggestedTvShow(
        tvShow = TvShowSample.Grimm,
        source = SuggestionSource.FromRated(TvShowWithPersonalRatingSample.Grimm.personalRating)
    )
}
