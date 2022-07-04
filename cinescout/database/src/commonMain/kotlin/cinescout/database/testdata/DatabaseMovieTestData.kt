package cinescout.database.testdata

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTmdbMovieId

object DatabaseMovieTestData {

    val Inception = DatabaseMovie(
        tmdbId = DatabaseTmdbMovieId(1),
        title = "Inception"
    )
}
