package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.ScreenplayWithGenreSlugsSample
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
        genreSlugs = ScreenplayWithGenreSlugsSample.BreakingBad.genreSlugs,
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.BreakingBad,
            trakt = TraktScreenplayIdSample.BreakingBad
        ),
        overview = ScreenplaySample.BreakingBad.overview,
        runtime = ScreenplaySample.BreakingBad.runtime.getOrNull(),
        status = ScreenplaySample.BreakingBad.status,
        title = ScreenplaySample.BreakingBad.title,
        voteAverage = ScreenplaySample.BreakingBad.rating.average.value,
        voteCount = ScreenplaySample.BreakingBad.rating.voteCount
    )
    val Dexter = TraktTvShowExtendedBody(
        airedEpisodes = ScreenplaySample.Dexter.airedEpisodes,
        firstAirDate = ScreenplaySample.Dexter.firstAirDate,
        genreSlugs = ScreenplayWithGenreSlugsSample.Dexter.genreSlugs,
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Dexter,
            trakt = TraktScreenplayIdSample.Dexter
        ),
        overview = ScreenplaySample.Dexter.overview,
        runtime = ScreenplaySample.Dexter.runtime.getOrNull(),
        status = ScreenplaySample.Dexter.status,
        title = ScreenplaySample.Dexter.title,
        voteAverage = ScreenplaySample.Dexter.rating.average.value,
        voteCount = ScreenplaySample.Dexter.rating.voteCount
    )
    val Grimm = TraktTvShowExtendedBody(
        airedEpisodes = ScreenplaySample.Grimm.airedEpisodes,
        firstAirDate = ScreenplaySample.Grimm.firstAirDate,
        genreSlugs = ScreenplayWithGenreSlugsSample.Grimm.genreSlugs,
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Grimm,
            trakt = TraktScreenplayIdSample.Grimm
        ),
        overview = ScreenplaySample.Grimm.overview,
        runtime = ScreenplaySample.Grimm.runtime.getOrNull(),
        status = ScreenplaySample.Grimm.status,
        title = ScreenplaySample.Grimm.title,
        voteAverage = ScreenplaySample.Grimm.rating.average.value,
        voteCount = ScreenplaySample.Grimm.rating.voteCount
    )

    val Inception = TraktMovieExtendedBody(
        genreSlugs = ScreenplayWithGenreSlugsSample.Inception.genreSlugs,
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.Inception,
            trakt = TraktScreenplayIdSample.Inception
        ),
        overview = ScreenplaySample.Inception.overview,
        releaseDate = ScreenplaySample.Inception.releaseDate.getOrNull(),
        runtime = ScreenplaySample.Inception.runtime.getOrNull(),
        tagline = ScreenplaySample.Inception.tagline.getOrNull().orEmpty(),
        title = ScreenplaySample.Inception.title,
        voteAverage = ScreenplaySample.Inception.rating.average.value,
        voteCount = ScreenplaySample.Inception.rating.voteCount
    )
    val TheWolfOfWallStreet = TraktMovieExtendedBody(
        genreSlugs = ScreenplayWithGenreSlugsSample.TheWolfOfWallStreet.genreSlugs,
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet,
            trakt = TraktScreenplayIdSample.TheWolfOfWallStreet
        ),
        overview = ScreenplaySample.TheWolfOfWallStreet.overview,
        releaseDate = ScreenplaySample.TheWolfOfWallStreet.releaseDate.getOrNull(),
        runtime = ScreenplaySample.TheWolfOfWallStreet.runtime.getOrNull(),
        tagline = ScreenplaySample.TheWolfOfWallStreet.tagline.getOrNull().orEmpty(),
        title = ScreenplaySample.TheWolfOfWallStreet.title,
        voteAverage = ScreenplaySample.TheWolfOfWallStreet.rating.average.value,
        voteCount = ScreenplaySample.TheWolfOfWallStreet.rating.voteCount
    )
    val War = TraktMovieExtendedBody(
        genreSlugs = ScreenplayWithGenreSlugsSample.War.genreSlugs,
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.War,
            trakt = TraktScreenplayIdSample.War
        ),
        overview = ScreenplaySample.War.overview,
        releaseDate = ScreenplaySample.War.releaseDate.getOrNull(),
        runtime = ScreenplaySample.War.runtime.getOrNull(),
        tagline = ScreenplaySample.War.tagline.getOrNull().orEmpty(),
        title = ScreenplaySample.War.title,
        voteAverage = ScreenplaySample.War.rating.average.value,
        voteCount = ScreenplaySample.War.rating.voteCount
    )
}
