package cinescout.movies.domain.testdata

import arrow.core.nonEmptyListOf
import cinescout.common.testdata.GenreTestData
import cinescout.movies.domain.model.MovieGenres
import cinescout.movies.domain.sample.TmdbMovieIdSample

object MovieGenresTestData {

    val Inception = MovieGenres(
        movieId = TmdbMovieIdSample.Inception,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieGenres(
        movieId = TmdbMovieIdSample.TheWolfOfWallStreet,
        genres = nonEmptyListOf(GenreTestData.Comedy, GenreTestData.Crime, GenreTestData.Drama)
    )

    val War = MovieGenres(
        movieId = TmdbMovieIdSample.War,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction)
    )
}
