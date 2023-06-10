package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplayGenre
import cinescout.database.model.id.DatabaseTmdbGenreId

object DatabaseScreenplayGenreSample {

    val Action = DatabaseScreenplayGenre(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(28)
    )

    val Adventure = DatabaseScreenplayGenre(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(12)
    )

    val ScienceFiction = DatabaseScreenplayGenre(
        screenplayId = DatabaseMovieSample.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(878)
    )
}
