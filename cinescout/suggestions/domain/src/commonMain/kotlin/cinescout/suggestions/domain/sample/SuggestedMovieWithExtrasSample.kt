package cinescout.suggestions.domain.sample

import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithExtrasSample
import cinescout.suggestions.domain.model.SuggestedMovieWithExtras

object SuggestedMovieWithExtrasSample {

    val Inception = SuggestedMovieWithExtras(
        movie = MovieSample.Inception,
        affinity = SuggestedMovieSample.Inception.affinity,
        genres = MovieWithExtrasSample.Inception.movieWithDetails.genres,
        credits = MovieWithExtrasSample.Inception.credits,
        isInWatchlist = MovieWithExtrasSample.Inception.isInWatchlist,
        keywords = MovieWithExtrasSample.Inception.keywords,
        personalRating = MovieWithExtrasSample.Inception.personalRating,
        source = SuggestedMovieSample.Inception.source
    )

    val TheWolfOfWallStreet = SuggestedMovieWithExtras(
        movie = MovieSample.TheWolfOfWallStreet,
        affinity = SuggestedMovieSample.TheWolfOfWallStreet.affinity,
        genres = MovieWithExtrasSample.TheWolfOfWallStreet.movieWithDetails.genres,
        credits = MovieWithExtrasSample.TheWolfOfWallStreet.credits,
        isInWatchlist = MovieWithExtrasSample.TheWolfOfWallStreet.isInWatchlist,
        keywords = MovieWithExtrasSample.TheWolfOfWallStreet.keywords,
        personalRating = MovieWithExtrasSample.TheWolfOfWallStreet.personalRating,
        source = SuggestedMovieSample.TheWolfOfWallStreet.source
    )

    val War = SuggestedMovieWithExtras(
        movie = MovieSample.War,
        affinity = SuggestedMovieSample.War.affinity,
        genres = MovieWithExtrasSample.War.movieWithDetails.genres,
        credits = MovieWithExtrasSample.War.credits,
        isInWatchlist = MovieWithExtrasSample.War.isInWatchlist,
        keywords = MovieWithExtrasSample.War.keywords,
        personalRating = MovieWithExtrasSample.War.personalRating,
        source = SuggestedMovieSample.War.source
    )
}
