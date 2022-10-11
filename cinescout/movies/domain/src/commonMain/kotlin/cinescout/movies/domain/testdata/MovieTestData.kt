package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.common.model.PublicRating
import cinescout.common.model.Rating
import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.getOrThrow
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.TmdbPosterImage
import com.soywiz.klock.Date

object MovieTestData {

    val Inception = Movie(
        backdropImage = TmdbBackdropImage("ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg").some(),
        overview = "A thief, who steals corporate secrets through use of dream-sharing technology, " +
            "is given the inverse task of planting an idea into the mind of a CEO.",
        posterImage = TmdbPosterImage("8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg").some(),
        rating = PublicRating(voteCount = 31_990, average = Rating.of(8.357).getOrThrow()),
        releaseDate = Date(year = 2010, month = 7, day = 15).some(),
        title = "Inception",
        tmdbId = TmdbMovieIdTestData.Inception
    )

    val TheWolfOfWallStreet = Movie(
        backdropImage = TmdbBackdropImage("blbA7NEHARQOWy5i9VF5K2kHrPc.jpg").some(),
        overview = "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker " +
            "living the high life to his fall involving crime, corruption and the federal government.",
        posterImage = TmdbPosterImage("pWHf4khOloNVfCxscsXFj3jj6gP.jpg").some(),
        rating = PublicRating(voteCount = 20_121, average = Rating.of(8.031).getOrThrow()),
        releaseDate = Date(year = 2013, month = 12, day = 25).some(),
        title = "The Wolf of Wall Street",
        tmdbId = TmdbMovieIdTestData.TheWolfOfWallStreet
    )

    val War = Movie(
        backdropImage = TmdbBackdropImage("5Tw0isY4Fs08burneYsa6JvHbER.jpg").some(),
        overview = "A story about two soldiers, one from North Korea and one from South Korea, " +
            "who are forced to work together to survive after their patrol is ambushed by enemy forces.",
        posterImage = TmdbPosterImage("7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg").some(),
        rating = PublicRating(voteCount = 166, average = Rating.of(6.8).getOrThrow()),
        releaseDate = Date(year = 2019, month = 2, day = 10).some(),
        title = "War",
        tmdbId = TmdbMovieIdTestData.War
    )
}
