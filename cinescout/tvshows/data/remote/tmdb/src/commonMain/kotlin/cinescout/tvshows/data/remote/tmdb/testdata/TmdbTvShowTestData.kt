package cinescout.tvshows.data.remote.tmdb.testdata

import cinescout.tvshows.data.remote.tmdb.model.TmdbTvShow
import cinescout.tvshows.domain.sample.TvShowSample

object TmdbTvShowTestData {

    val BreakingBad = TmdbTvShow(
        backdropPath = TvShowSample.BreakingBad.backdropImage.orNull()?.path,
        firstAirDate = TvShowSample.BreakingBad.firstAirDate,
        id = TvShowSample.BreakingBad.tmdbId,
        title = TvShowSample.BreakingBad.title,
        overview = TvShowSample.BreakingBad.overview,
        posterPath = TvShowSample.BreakingBad.posterImage.orNull()?.path,
        voteCount = TvShowSample.BreakingBad.rating.voteCount,
        voteAverage = TvShowSample.BreakingBad.rating.average.value
    )

    val Dexter = TmdbTvShow(
        backdropPath = TvShowSample.Dexter.backdropImage.orNull()?.path,
        firstAirDate = TvShowSample.Dexter.firstAirDate,
        id = TvShowSample.Dexter.tmdbId,
        title = TvShowSample.Dexter.title,
        overview = TvShowSample.Dexter.overview,
        posterPath = TvShowSample.Dexter.posterImage.orNull()?.path,
        voteCount = TvShowSample.Dexter.rating.voteCount,
        voteAverage = TvShowSample.Dexter.rating.average.value
    )

    val Grimm = TmdbTvShow(
        backdropPath = TvShowSample.Grimm.backdropImage.orNull()?.path,
        firstAirDate = TvShowSample.Grimm.firstAirDate,
        id = TvShowSample.Grimm.tmdbId,
        title = TvShowSample.Grimm.title,
        overview = TvShowSample.Grimm.overview,
        posterPath = TvShowSample.Grimm.posterImage.orNull()?.path,
        voteCount = TvShowSample.Grimm.rating.voteCount,
        voteAverage = TvShowSample.Grimm.rating.average.value
    )
}
