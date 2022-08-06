package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbBackdropImage
import cinescout.movies.domain.model.TmdbPosterImage
import cinescout.movies.domain.model.getOrThrow
import com.soywiz.klock.Date

object MovieTestData {

    val Inception = Movie(
        backdropImage = TmdbBackdropImage("ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg").some(),
        posterImage = TmdbPosterImage("8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg"),
        rating = MovieRating(voteCount = 31_990, average = Rating.of(8.357).getOrThrow()),
        releaseDate = Date(year = 2010, month = 7, day = 15),
        title = "Inception",
        tmdbId = TmdbMovieIdTestData.Inception
    )

    val TheWolfOfWallStreet = Movie(
        backdropImage = TmdbBackdropImage("blbA7NEHARQOWy5i9VF5K2kHrPc.jpg").some(),
        posterImage = TmdbPosterImage("pWHf4khOloNVfCxscsXFj3jj6gP.jpg"),
        rating = MovieRating(voteCount = 20_121, average = Rating.of(8.031).getOrThrow()),
        releaseDate = Date(year = 2013, month = 12, day = 25),
        title = "The Wolf of Wall Street",
        tmdbId = TmdbMovieIdTestData.TheWolfOfWallStreet
    )

    val War = Movie(
        backdropImage = TmdbBackdropImage("5Tw0isY4Fs08burneYsa6JvHbER.jpg").some(),
        posterImage = TmdbPosterImage("7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg"),
        rating = MovieRating(voteCount = 166, average = Rating.of(6.8).getOrThrow()),
        releaseDate = Date(year = 2019, month = 2, day = 10),
        title = "War",
        tmdbId = TmdbMovieIdTestData.War
    )
}
