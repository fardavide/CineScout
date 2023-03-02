package cinescout.movies.domain.testdata

import arrow.core.nonEmptyListOf
import cinescout.movies.domain.model.MovieGenres
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.screenplay.domain.sample.GenreSample

object MovieGenresTestData {

    val Inception = MovieGenres(
        movieId = TmdbMovieIdSample.Inception,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )

    val TheWolfOfWallStreet = MovieGenres(
        movieId = TmdbMovieIdSample.TheWolfOfWallStreet,
        genres = nonEmptyListOf(GenreSample.Comedy, GenreSample.Crime, GenreSample.Drama)
    )

    val War = MovieGenres(
        movieId = TmdbMovieIdSample.War,
        genres = nonEmptyListOf(GenreSample.Action, GenreSample.Adventure, GenreSample.ScienceFiction)
    )
}
