package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieRating

object MovieRatingTestData {

    val Inception = MovieRating(
        tmdbId = MovieWithRatingTestData.Inception.movie.tmdbId,
        rating = MovieWithRatingTestData.Inception.rating
    )

    val TheWolfOfWallStreet = MovieRating(
        tmdbId = MovieWithRatingTestData.TheWolfOfWallStreet.movie.tmdbId,
        rating = MovieWithRatingTestData.TheWolfOfWallStreet.rating
    )

    val War = MovieRating(
        tmdbId = MovieWithRatingTestData.War.movie.tmdbId,
        rating = MovieWithRatingTestData.War.rating
    )
}
