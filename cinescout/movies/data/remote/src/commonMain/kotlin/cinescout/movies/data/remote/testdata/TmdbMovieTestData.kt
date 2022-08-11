package cinescout.movies.data.remote.testdata

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.testdata.MovieTestData

object TmdbMovieTestData {

    val Inception = TmdbMovie(
        backdropPath = MovieTestData.Inception.backdropImage.orNull()?.path,
        id = MovieTestData.Inception.tmdbId,
        posterPath = MovieTestData.Inception.posterImage.orNull()?.path,
        releaseDate = MovieTestData.Inception.releaseDate.orNull(),
        title = MovieTestData.Inception.title,
        voteCount = MovieTestData.Inception.rating.voteCount,
        voteAverage = MovieTestData.Inception.rating.average.value
    )
    val TheWolfOfWallStreet = TmdbMovie(
        backdropPath = MovieTestData.TheWolfOfWallStreet.backdropImage.orNull()?.path,
        id = MovieTestData.TheWolfOfWallStreet.tmdbId,
        posterPath = MovieTestData.TheWolfOfWallStreet.posterImage.orNull()?.path,
        releaseDate = MovieTestData.TheWolfOfWallStreet.releaseDate.orNull(),
        title = MovieTestData.TheWolfOfWallStreet.title,
        voteCount = MovieTestData.TheWolfOfWallStreet.rating.voteCount,
        voteAverage = MovieTestData.TheWolfOfWallStreet.rating.average.value
    )
}
