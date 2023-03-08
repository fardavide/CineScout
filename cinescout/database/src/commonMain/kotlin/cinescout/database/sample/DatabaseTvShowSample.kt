package cinescout.database.sample

import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTvShow
import com.soywiz.klock.Date

object DatabaseTvShowSample {

    val BreakingBad = DatabaseTvShow(
        backdropPath = "84XPpjGvxNyExjSuLQe0SzioErt.jpg",
        firstAirDate = Date(year = 2008, month = 1, day = 20),
        overview = "High school chemistry teacher Walter White's life is suddenly transformed by a dire " +
            "medical diagnosis. Street-savvy former student Jesse Pinkman teaches Walter a new trade.",
        posterPath = "ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
        ratingAverage = 8.839,
        ratingCount = 10_125,
        title = "Breaking Bad",
        tmdbId = DatabaseTmdbTvShowId(1_396)
    )

    val Grimm = DatabaseTvShow(
        backdropPath = "oS3nip9GGsx5A7vWp8A1cazqJlF.jpg",
        firstAirDate = Date(year = 2011, month = 10, day = 28),
        overview = "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite " +
            "line of criminal profilers known as \"Grimms,\" he increasingly finds his responsibilities as a " +
            "detective at odds with his new responsibilities as a Grimm.",
        posterPath = "iOptnt1QHi6bIHmOq6adnZTV0bU.jpg",
        ratingAverage = 8.259,
        ratingCount = 2_613,
        title = "Grimm",
        tmdbId = DatabaseTmdbTvShowId(39_351)
    )
}
