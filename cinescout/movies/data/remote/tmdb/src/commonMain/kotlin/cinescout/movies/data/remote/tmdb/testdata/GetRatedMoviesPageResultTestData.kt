package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.sample.TmdbMovieSample
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies

object GetRatedMoviesPageResultTestData {

    val Inception = GetRatedMovies.Response.PageResult(
        backdropPath = TmdbMovieSample.Inception.backdropPath,
        id = TmdbMovieSample.Inception.id,
        overview = TmdbMovieSample.Inception.overview,
        posterPath = TmdbMovieSample.Inception.posterPath,
        rating = 9.0,
        releaseDate = TmdbMovieSample.Inception.releaseDate,
        title = TmdbMovieSample.Inception.title,
        voteAverage = TmdbMovieSample.Inception.voteAverage,
        voteCount = TmdbMovieSample.Inception.voteCount
    )
}
