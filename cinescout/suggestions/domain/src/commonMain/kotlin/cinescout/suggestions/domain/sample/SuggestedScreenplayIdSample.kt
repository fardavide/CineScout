package cinescout.suggestions.domain.sample

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionSource

object SuggestedScreenplayIdSample {

    val BreakingBad = SuggestedTvShowId(
        source = SuggestionSource.Popular,
        tvShowId = TmdbScreenplayIdSample.BreakingBad
    )

    val Dexter = SuggestedTvShowId(
        source = SuggestionSource.Trending,
        tvShowId = TmdbScreenplayIdSample.Dexter
    )

    val Grimm = SuggestedTvShowId(
        source = SuggestionSource.Suggested,
        tvShowId = TmdbScreenplayIdSample.Grimm
    )

    val Inception = SuggestedMovieId(
        movieId = TmdbScreenplayIdSample.Inception,
        source = SuggestionSource.Suggested
    )

    val TheWolfOfWallStreet = SuggestedMovieId(
        movieId = TmdbScreenplayIdSample.TheWolfOfWallStreet,
        source = SuggestionSource.Trending
    )

    val War = SuggestedMovieId(
        movieId = TmdbScreenplayIdSample.War,
        source = SuggestionSource.Popular
    )
}
