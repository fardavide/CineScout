package cinescout.suggestions.domain.sample

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionSource

object SuggestedTvShowIdSample {

    val BreakingBad = SuggestedTvShowId(
        tvShowId = TmdbScreenplayIdSample.BreakingBad,
        source = SuggestionSource.Popular
    )

    val Dexter = SuggestedTvShowId(
        tvShowId = TmdbScreenplayIdSample.Dexter,
        source = SuggestionSource.Trending
    )

    val Grimm = SuggestedTvShowId(
        tvShowId = TmdbScreenplayIdSample.Grimm,
        source = SuggestionSource.Suggested
    )
}
