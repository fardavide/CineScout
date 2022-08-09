package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieGenre
import cinescout.database.model.DatabaseTmdbGenreId

object DatabaseMovieGenreTestData {

    val Action = DatabaseMovieGenre(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(28),
    )

    val Adventure = DatabaseMovieGenre(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(12),
    )

    val ScienceFiction = DatabaseMovieGenre(
        movieId = DatabaseMovieTestData.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(878),
    )
}
