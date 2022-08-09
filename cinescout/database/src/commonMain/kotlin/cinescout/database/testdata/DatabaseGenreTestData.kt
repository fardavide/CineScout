package cinescout.database.testdata

import cinescout.database.model.DatabaseGenre
import cinescout.database.model.DatabaseTmdbGenreId

object DatabaseGenreTestData {

    val Action = DatabaseGenre(
        tmdbId = DatabaseTmdbGenreId(28),
        name = "Action"
    )

    val Adventure = DatabaseGenre(
        tmdbId = DatabaseTmdbGenreId(12),
        name = "Adventure"
    )

    val Comedy = DatabaseGenre(
        tmdbId = DatabaseTmdbGenreId(35),
        name = "Comedy"
    )

    val Crime = DatabaseGenre(
        tmdbId = DatabaseTmdbGenreId(80),
        name = "Crime"
    )

    val Drama = DatabaseGenre(
        tmdbId = DatabaseTmdbGenreId(18),
        name = "Drama"
    )

    val ScienceFiction = DatabaseGenre(
        tmdbId = DatabaseTmdbGenreId(878),
        name = "Science Fiction"
    )
}
