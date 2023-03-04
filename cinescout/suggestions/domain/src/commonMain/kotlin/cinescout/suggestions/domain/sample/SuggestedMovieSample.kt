package cinescout.suggestions.domain.sample

import cinescout.movies.domain.sample.MovieSample
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestionSource

object SuggestedMovieSample {

    val Inception = SuggestedMovie(
        movie = MovieSample.Inception,
        source = SuggestionSource.Suggested
    )

    val TheWolfOfWallStreet = SuggestedMovie(
        movie = MovieSample.TheWolfOfWallStreet,
        source = SuggestionSource.Trending
    )

    val War = SuggestedMovie(
        movie = MovieSample.War,
        source = SuggestionSource.Popular
    )
}
