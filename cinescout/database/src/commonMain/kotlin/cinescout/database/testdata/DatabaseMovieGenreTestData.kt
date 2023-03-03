package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieGenre
import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.sample.DatabaseMovieSample

object DatabaseMovieGenreTestData {

    val Action = DatabaseMovieGenre(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(28)
    )

    val Adventure = DatabaseMovieGenre(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(12)
    )

    val ScienceFiction = DatabaseMovieGenre(
        movieId = DatabaseMovieSample.Inception.tmdbId,
        genreId = DatabaseTmdbGenreId(878)
    )
}
