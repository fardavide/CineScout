package cinescout.suggestions.domain.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestionSource

object SuggestedScreenplaySample {

    val BreakingBad = SuggestedTvShow(
        tvShow = ScreenplaySample.BreakingBad,
        source = SuggestionSource.Popular
    )

    val Dexter = SuggestedTvShow(
        tvShow = ScreenplaySample.Dexter,
        source = SuggestionSource.Trending
    )

    val Grimm = SuggestedTvShow(
        tvShow = ScreenplaySample.Grimm,
        source = SuggestionSource.Recommended
    )

    val Inception = SuggestedMovie(
        movie = ScreenplaySample.Inception,
        source = SuggestionSource.Recommended
    )

    val TheWolfOfWallStreet = SuggestedMovie(
        movie = ScreenplaySample.TheWolfOfWallStreet,
        source = SuggestionSource.Trending
    )

    val War = SuggestedMovie(
        movie = ScreenplaySample.War,
        source = SuggestionSource.Popular
    )
}
