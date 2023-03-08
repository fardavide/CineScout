package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.sample.TmdbMovieSample
import cinescout.movies.data.remote.tmdb.model.SearchMovie

object SearchMoviePageResultTestData {

    val Inception = SearchMovie.Response.PageResult(
        backdropPath = TmdbMovieSample.Inception.backdropPath,
        id = TmdbMovieSample.Inception.id,
        overview = TmdbMovieSample.Inception.overview,
        posterPath = TmdbMovieSample.Inception.posterPath,
        releaseDate = TmdbMovieSample.Inception.releaseDate,
        title = TmdbMovieSample.Inception.title,
        voteAverage = TmdbMovieSample.Inception.voteAverage,
        voteCount = TmdbMovieSample.Inception.voteCount
    )
}
