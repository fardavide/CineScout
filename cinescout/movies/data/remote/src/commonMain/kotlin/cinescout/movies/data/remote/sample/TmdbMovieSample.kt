package cinescout.movies.data.remote.sample

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.sample.MovieSample

object TmdbMovieSample {

    val Inception = TmdbMovie(
        backdropPath = MovieSample.Inception.backdropImage.orNull()?.path,
        id = MovieSample.Inception.tmdbId,
        overview = MovieSample.Inception.overview,
        posterPath = MovieSample.Inception.posterImage.orNull()?.path,
        releaseDate = MovieSample.Inception.releaseDate.orNull(),
        title = MovieSample.Inception.title,
        voteCount = MovieSample.Inception.rating.voteCount,
        voteAverage = MovieSample.Inception.rating.average.value
    )

    val TheWolfOfWallStreet = TmdbMovie(
        backdropPath = MovieSample.TheWolfOfWallStreet.backdropImage.orNull()?.path,
        id = MovieSample.TheWolfOfWallStreet.tmdbId,
        overview = MovieSample.TheWolfOfWallStreet.overview,
        posterPath = MovieSample.TheWolfOfWallStreet.posterImage.orNull()?.path,
        releaseDate = MovieSample.TheWolfOfWallStreet.releaseDate.orNull(),
        title = MovieSample.TheWolfOfWallStreet.title,
        voteCount = MovieSample.TheWolfOfWallStreet.rating.voteCount,
        voteAverage = MovieSample.TheWolfOfWallStreet.rating.average.value
    )

    val War = TmdbMovie(
        backdropPath = MovieSample.War.backdropImage.orNull()?.path,
        id = MovieSample.War.tmdbId,
        overview = MovieSample.War.overview,
        posterPath = MovieSample.War.posterImage.orNull()?.path,
        releaseDate = MovieSample.War.releaseDate.orNull(),
        title = MovieSample.War.title,
        voteCount = MovieSample.War.rating.voteCount,
        voteAverage = MovieSample.War.rating.average.value
    )
}
