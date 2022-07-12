package cinescout.database.testdata

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTmdbMovieId
import com.soywiz.klock.Date

object DatabaseMovieTestData {

    val Inception = DatabaseMovie(
        tmdbId = DatabaseTmdbMovieId(1),
        releaseDate = Date(year = 2010, month = 7, day = 15),
        title = "Inception"
    )
}
