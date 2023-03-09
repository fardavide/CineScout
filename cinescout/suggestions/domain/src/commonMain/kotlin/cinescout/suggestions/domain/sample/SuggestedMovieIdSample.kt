package cinescout.suggestions.domain.sample

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestionSource

object SuggestedMovieIdSample {

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
