package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.SearchMovie

object SearchMoviePageResultTestData {

    val Inception = SearchMovie.Response.PageResult(
        backdropPath = TmdbMovieTestData.Inception.backdropPath,
        id = TmdbMovieTestData.Inception.id,
        overview = TmdbMovieTestData.Inception.overview,
        posterPath = TmdbMovieTestData.Inception.posterPath,
        releaseDate = TmdbMovieTestData.Inception.releaseDate,
        title = TmdbMovieTestData.Inception.title,
        voteAverage = TmdbMovieTestData.Inception.voteAverage,
        voteCount = TmdbMovieTestData.Inception.voteCount
    )
}
