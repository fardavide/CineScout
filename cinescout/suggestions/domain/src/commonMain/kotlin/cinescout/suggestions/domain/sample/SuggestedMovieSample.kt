package cinescout.suggestions.domain.sample

import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestionSource

object SuggestedMovieSample {

    val Inception = SuggestedMovie(
        movie = MovieSample.Inception,
        source = SuggestionSource.FromRated(MovieWithPersonalRatingSample.Inception.personalRating)
    )

    val TheWolfOfWallStreet = SuggestedMovie(
        movie = MovieSample.TheWolfOfWallStreet,
        source = SuggestionSource.FromRated(MovieWithPersonalRatingSample.TheWolfOfWallStreet.personalRating)
    )

    val War = SuggestedMovie(
        movie = MovieSample.War,
        source = SuggestionSource.FromRated(MovieWithPersonalRatingSample.War.personalRating)
    )
}
