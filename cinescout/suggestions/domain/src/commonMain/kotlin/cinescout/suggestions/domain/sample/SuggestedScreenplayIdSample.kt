package cinescout.suggestions.domain.sample

import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionSource

object SuggestedScreenplayIdSample {

    val BreakingBad = SuggestedTvShowId(
        source = SuggestionSource.Popular,
        tvShowIds = ScreenplayIdsSample.BreakingBad
    )

    val Dexter = SuggestedTvShowId(
        source = SuggestionSource.Trending,
        tvShowIds = ScreenplayIdsSample.Dexter
    )

    val Grimm = SuggestedTvShowId(
        source = SuggestionSource.Suggested,
        tvShowIds = ScreenplayIdsSample.Grimm
    )

    val Inception = SuggestedMovieId(
        movieIds = ScreenplayIdsSample.Inception,
        source = SuggestionSource.Suggested
    )

    val TheWolfOfWallStreet = SuggestedMovieId(
        movieIds = ScreenplayIdsSample.TheWolfOfWallStreet,
        source = SuggestionSource.Trending
    )

    val War = SuggestedMovieId(
        movieIds = ScreenplayIdsSample.War,
        source = SuggestionSource.Popular
    )
}
