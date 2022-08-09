package cinescout.movies.domain.testdata

import arrow.core.nonEmptyListOf
import cinescout.movies.domain.model.MovieGenres

object MovieGenresTestData {

    val Inception = MovieGenres(
        movieId = TmdbMovieIdTestData.Inception,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieGenres(
        movieId = TmdbMovieIdTestData.TheWolfOfWallStreet,
        genres = nonEmptyListOf(GenreTestData.Comedy, GenreTestData.Crime, GenreTestData.Drama)
    )

    val War = MovieGenres(
        movieId = TmdbMovieIdTestData.War,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )
}
