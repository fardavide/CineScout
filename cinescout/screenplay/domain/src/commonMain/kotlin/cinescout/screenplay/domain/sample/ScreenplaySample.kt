package cinescout.screenplay.domain.sample

import arrow.core.some
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.PublicRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.model.getOrThrow
import com.soywiz.klock.Date

object ScreenplaySample {

    val Avatar3 = Movie(
        releaseDate = Date(year = 2024, month = 12, day = 20).some(),
        ids = ScreenplayIdsSample.Avatar3,
        overview = "The third entry in the Avatar franchise.",
        rating = PublicRating(voteCount = 51, average = Rating.of(6.82353).getOrThrow()),
        title = "Avatar 3"
    )

    val BreakingBad = TvShow(
        firstAirDate = Date(year = 2008, month = 1, day = 21),
        ids = ScreenplayIdsSample.BreakingBad,
        overview = "When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer " +
            "and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness " +
            "and an unrelenting desire to secure his family's financial future at any cost as he enters the " +
            "dangerous world of drugs and crime.",
        rating = PublicRating(voteCount = 85_617, average = Rating.of(9.2584).getOrThrow()),
        title = "Breaking Bad"
    )
    val Dexter = TvShow(
        firstAirDate = Date(year = 2006, month = 10, day = 1),
        ids = ScreenplayIdsSample.Dexter,
        overview = "Dexter Morgan, a blood spatter pattern analyst for the Miami Metro Police also leads a " +
            "secret life as a serial killer, hunting down criminals who have slipped through the cracks of justice.",
        rating = PublicRating(voteCount = 3_233, average = Rating.of(8.191).getOrThrow()),
        title = "Dexter"
    )
    val Grimm = TvShow(
        firstAirDate = Date(year = 2011, month = 10, day = 28),
        ids = ScreenplayIdsSample.Grimm,
        overview = "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite " +
            "line of criminal profilers known as Grimms, he increasingly finds his responsibilities as a " +
            "detective at odds with his new responsibilities as a Grimm.",
        rating = PublicRating(voteCount = 2_613, average = Rating.of(8.259).getOrThrow()),
        title = "Grimm"
    )
    val Inception = Movie(
        ids = ScreenplayIdsSample.Inception,
        overview = "A thief, who steals corporate secrets through use of dream-sharing technology, " +
            "is given the inverse task of planting an idea into the mind of a CEO.",
        rating = PublicRating(voteCount = 31_990, average = Rating.of(8.357).getOrThrow()),
        releaseDate = Date(year = 2010, month = 7, day = 15).some(),
        title = "Inception"
    )
    val TheWalkingDeadDeadCity = TvShow(
        firstAirDate = Date(year = 2023, month = 6, day = 18),
        ids = ScreenplayIdsSample.TheWalkingDeadDeadCity,
        overview = "Maggie and Negan travel to a post-apocalyptic Manhattan, long ago cut off from the mainland. " +
            "The crumbling city is filled with the dead and denizens who have made New York City their own world " +
            "full of anarchy, danger, beauty, and terror.",
        rating = PublicRating(voteCount = 17, average = Rating.of(6.64706).getOrThrow()),
        title = "The Walking Dead: Dead City"
    )
    val TheWolfOfWallStreet = Movie(
        ids = ScreenplayIdsSample.TheWolfOfWallStreet,
        overview = "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker " +
            "living the high life to his fall involving crime, corruption and the federal government.",
        rating = PublicRating(voteCount = 20_121, average = Rating.of(8.031).getOrThrow()),
        releaseDate = Date(year = 2013, month = 12, day = 25).some(),
        title = "The Wolf of Wall Street"
    )
    val War = Movie(
        ids = ScreenplayIdsSample.War,
        overview = "Khalid, is entrusted with the task of eliminating Kabir, a former soldier turned rogue, as he " +
            "engages in an epic battle with his mentor who had taught him everything.",
        rating = PublicRating(voteCount = 166, average = Rating.of(6.8).getOrThrow()),
        releaseDate = Date(year = 2019, month = 2, day = 10).some(),
        title = "War"
    )
}
