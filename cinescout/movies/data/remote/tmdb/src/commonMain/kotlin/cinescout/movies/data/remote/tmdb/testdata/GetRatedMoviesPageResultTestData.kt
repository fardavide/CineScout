package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies

object GetRatedMoviesPageResultTestData {

    val Inception = GetRatedMovies.Response.PageResult(
        backdropPath = TmdbMovieTestData.Inception.backdropPath,
        id = TmdbMovieTestData.Inception.id,
        posterPath = TmdbMovieTestData.Inception.posterPath,
        rating = 9.0,
        releaseDate = TmdbMovieTestData.Inception.releaseDate,
        title = TmdbMovieTestData.Inception.title,
        voteAverage = TmdbMovieTestData.Inception.voteAverage,
        voteCount = TmdbMovieTestData.Inception.voteCount
    )
}
