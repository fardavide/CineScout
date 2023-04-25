package cinescout.screenplay.data.remote.tmdb.sample

import cinescout.screenplay.data.remote.tmdb.model.TmdbMovie
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample

object TmdbMovieSample {

    val Avatar3 = TmdbMovie(
        id = TmdbScreenplayIdSample.Avatar3,
        overview = ScreenplaySample.Avatar3.overview,
        releaseDate = ScreenplaySample.Avatar3.releaseDate.orNull(),
        title = ScreenplaySample.Avatar3.title,
        voteCount = ScreenplaySample.Avatar3.rating.voteCount,
        voteAverage = ScreenplaySample.Avatar3.rating.average.value
    )

    val Inception = TmdbMovie(
        id = TmdbScreenplayIdSample.Inception,
        overview = ScreenplaySample.Inception.overview,
        releaseDate = ScreenplaySample.Inception.releaseDate.orNull(),
        title = ScreenplaySample.Inception.title,
        voteCount = ScreenplaySample.Inception.rating.voteCount,
        voteAverage = ScreenplaySample.Inception.rating.average.value
    )

    val TheWolfOfWallStreet = TmdbMovie(
        id = TmdbScreenplayIdSample.TheWolfOfWallStreet,
        overview = ScreenplaySample.TheWolfOfWallStreet.overview,
        releaseDate = ScreenplaySample.TheWolfOfWallStreet.releaseDate.orNull(),
        title = ScreenplaySample.TheWolfOfWallStreet.title,
        voteCount = ScreenplaySample.TheWolfOfWallStreet.rating.voteCount,
        voteAverage = ScreenplaySample.TheWolfOfWallStreet.rating.average.value
    )

    val War = TmdbMovie(
        id = TmdbScreenplayIdSample.War,
        overview = ScreenplaySample.War.overview,
        releaseDate = ScreenplaySample.War.releaseDate.orNull(),
        title = ScreenplaySample.War.title,
        voteCount = ScreenplaySample.War.rating.voteCount,
        voteAverage = ScreenplaySample.War.rating.average.value
    )
}
