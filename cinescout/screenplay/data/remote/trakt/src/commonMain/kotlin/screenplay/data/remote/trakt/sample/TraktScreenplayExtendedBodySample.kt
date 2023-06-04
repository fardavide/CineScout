package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody
import screenplay.data.remote.trakt.model.TraktTvShowIds

object TraktScreenplayExtendedBodySample {

    val BreakingBad = TraktTvShowExtendedBody(
        airedEpisodes = ScreenplaySample.BreakingBad.airedEpisodes,
        firstAirDate = ScreenplaySample.BreakingBad.firstAirDate,
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.BreakingBad,
            trakt = TraktScreenplayIdSample.BreakingBad
        ),
        overview = ScreenplaySample.BreakingBad.overview,
        runtime = ScreenplaySample.BreakingBad.runtime.orNull(),
        title = ScreenplaySample.BreakingBad.title,
        voteAverage = ScreenplaySample.BreakingBad.rating.average.value,
        voteCount = ScreenplaySample.BreakingBad.rating.voteCount
    )
    val Dexter = TraktTvShowExtendedBody(
        airedEpisodes = ScreenplaySample.Dexter.airedEpisodes,
        firstAirDate = ScreenplaySample.Dexter.firstAirDate,
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Dexter,
            trakt = TraktScreenplayIdSample.Dexter
        ),
        overview = ScreenplaySample.Dexter.overview,
        runtime = ScreenplaySample.Dexter.runtime.orNull(),
        title = ScreenplaySample.Dexter.title,
        voteAverage = ScreenplaySample.Dexter.rating.average.value,
        voteCount = ScreenplaySample.Dexter.rating.voteCount
    )
    val Grimm = TraktTvShowExtendedBody(
        airedEpisodes = ScreenplaySample.Grimm.airedEpisodes,
        firstAirDate = ScreenplaySample.Grimm.firstAirDate,
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Grimm,
            trakt = TraktScreenplayIdSample.Grimm
        ),
        overview = ScreenplaySample.Grimm.overview,
        runtime = ScreenplaySample.Grimm.runtime.orNull(),
        title = ScreenplaySample.Grimm.title,
        voteAverage = ScreenplaySample.Grimm.rating.average.value,
        voteCount = ScreenplaySample.Grimm.rating.voteCount
    )

    val Inception = TraktMovieExtendedBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.Inception,
            trakt = TraktScreenplayIdSample.Inception
        ),
        overview = ScreenplaySample.Inception.overview,
        releaseDate = ScreenplaySample.Inception.releaseDate.orNull(),
        runtime = ScreenplaySample.Inception.runtime.orNull(),
        tagline = ScreenplaySample.Inception.tagline.orNull().orEmpty(),
        title = ScreenplaySample.Inception.title,
        voteAverage = ScreenplaySample.Inception.rating.average.value,
        voteCount = ScreenplaySample.Inception.rating.voteCount
    )
    val TheWolfOfWallStreet = TraktMovieExtendedBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet,
            trakt = TraktScreenplayIdSample.TheWolfOfWallStreet
        ),
        overview = ScreenplaySample.TheWolfOfWallStreet.overview,
        releaseDate = ScreenplaySample.TheWolfOfWallStreet.releaseDate.orNull(),
        runtime = ScreenplaySample.TheWolfOfWallStreet.runtime.orNull(),
        tagline = ScreenplaySample.TheWolfOfWallStreet.tagline.orNull().orEmpty(),
        title = ScreenplaySample.TheWolfOfWallStreet.title,
        voteAverage = ScreenplaySample.TheWolfOfWallStreet.rating.average.value,
        voteCount = ScreenplaySample.TheWolfOfWallStreet.rating.voteCount
    )
    val War = TraktMovieExtendedBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.War,
            trakt = TraktScreenplayIdSample.War
        ),
        overview = ScreenplaySample.War.overview,
        releaseDate = ScreenplaySample.War.releaseDate.orNull(),
        runtime = ScreenplaySample.War.runtime.orNull(),
        tagline = ScreenplaySample.War.tagline.orNull().orEmpty(),
        title = ScreenplaySample.War.title,
        voteAverage = ScreenplaySample.War.rating.average.value,
        voteCount = ScreenplaySample.War.rating.voteCount
    )
}
