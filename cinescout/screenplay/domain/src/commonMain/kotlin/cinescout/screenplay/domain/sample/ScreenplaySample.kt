package cinescout.screenplay.domain.sample

import arrow.core.some
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date

object ScreenplaySample {

    val BreakingBad = TvShow(
        firstAirDate = Date(year = 2008, month = 1, day = 20),
        overview = "High school chemistry teacher Walter White's life is suddenly transformed by a dire " +
            "medical diagnosis. Street-savvy former student Jesse Pinkman teaches Walter a new trade.",
        rating = PublicRating(voteCount = 10_125, average = Rating.of(8.839).getOrThrow()),
        title = "Breaking Bad",
        tmdbId = TmdbScreenplayIdSample.BreakingBad
    )
    val Dexter = TvShow(
        firstAirDate = Date(year = 2006, month = 10, day = 1),
        overview = "Dexter Morgan, a blood spatter pattern analyst for the Miami Metro Police also leads a " +
            "secret life as a serial killer, hunting down criminals who have slipped through the cracks of justice.",
        rating = PublicRating(voteCount = 3_233, average = Rating.of(8.191).getOrThrow()),
        title = "Dexter",
        tmdbId = TmdbScreenplayIdSample.Dexter
    )
    val Grimm = TvShow(
        firstAirDate = Date(year = 2011, month = 10, day = 28),
        overview = "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite " +
            "line of criminal profilers known as Grimms, he increasingly finds his responsibilities as a " +
            "detective at odds with his new responsibilities as a Grimm.",
        rating = PublicRating(voteCount = 2_613, average = Rating.of(8.259).getOrThrow()),
        title = "Grimm",
        tmdbId = TmdbScreenplayIdSample.Grimm
    )
    val Inception = Movie(
        overview = "A thief, who steals corporate secrets through use of dream-sharing technology, " +
            "is given the inverse task of planting an idea into the mind of a CEO.",
        rating = PublicRating(voteCount = 31_990, average = Rating.of(8.357).getOrThrow()),
        releaseDate = Date(year = 2010, month = 7, day = 15).some(),
        title = "Inception",
        tmdbId = TmdbScreenplayIdSample.Inception
    )
    val TheWolfOfWallStreet = Movie(
        overview = "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker " +
            "living the high life to his fall involving crime, corruption and the federal government.",
        rating = PublicRating(voteCount = 20_121, average = Rating.of(8.031).getOrThrow()),
        releaseDate = Date(year = 2013, month = 12, day = 25).some(),
        title = "The Wolf of Wall Street",
        tmdbId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )
    val War = Movie(
        overview = "A story about two soldiers, one from North Korea and one from South Korea, " +
            "who are forced to work together to survive after their patrol is ambushed by enemy forces.",
        rating = PublicRating(voteCount = 166, average = Rating.of(6.8).getOrThrow()),
        releaseDate = Date(year = 2019, month = 2, day = 10).some(),
        title = "War",
        tmdbId = TmdbScreenplayIdSample.War
    )
}
