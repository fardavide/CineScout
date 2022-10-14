package cinescout.database.testdata

import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTvShow
import com.soywiz.klock.Date

object DatabaseTvShowTestData {

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
