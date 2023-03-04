package cinescout.suggestions.domain.sample

import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.tvshows.domain.sample.TvShowSample

object SuggestedTvShowSample {

    val BreakingBad = SuggestedTvShow(
        tvShow = TvShowSample.BreakingBad,
        source = SuggestionSource.Popular
    )

    val Dexter = SuggestedTvShow(
        tvShow = TvShowSample.Dexter,
        source = SuggestionSource.Trending
    )

    val Grimm = SuggestedTvShow(
        tvShow = TvShowSample.Grimm,
        source = SuggestionSource.Suggested
    )
}
