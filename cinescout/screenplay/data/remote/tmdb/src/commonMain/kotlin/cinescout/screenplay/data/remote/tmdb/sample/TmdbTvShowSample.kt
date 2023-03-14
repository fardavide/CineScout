package cinescout.screenplay.data.remote.tmdb.sample

import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.domain.sample.ScreenplaySample

object TmdbTvShowSample {

    val BreakingBad = TmdbTvShow(
        firstAirDate = ScreenplaySample.BreakingBad.firstAirDate,
        id = ScreenplaySample.BreakingBad.tmdbId,
        title = ScreenplaySample.BreakingBad.title,
        overview = ScreenplaySample.BreakingBad.overview,
        voteCount = ScreenplaySample.BreakingBad.rating.voteCount,
        voteAverage = ScreenplaySample.BreakingBad.rating.average.value
    )

    val Dexter = TmdbTvShow(
        firstAirDate = ScreenplaySample.Dexter.firstAirDate,
        id = ScreenplaySample.Dexter.tmdbId,
        title = ScreenplaySample.Dexter.title,
        overview = ScreenplaySample.Dexter.overview,
        voteCount = ScreenplaySample.Dexter.rating.voteCount,
        voteAverage = ScreenplaySample.Dexter.rating.average.value
    )

    val Grimm = TmdbTvShow(
        firstAirDate = ScreenplaySample.Grimm.firstAirDate,
        id = ScreenplaySample.Grimm.tmdbId,
        title = ScreenplaySample.Grimm.title,
        overview = ScreenplaySample.Grimm.overview,
        voteCount = ScreenplaySample.Grimm.rating.voteCount,
        voteAverage = ScreenplaySample.Grimm.rating.average.value
    )
}
