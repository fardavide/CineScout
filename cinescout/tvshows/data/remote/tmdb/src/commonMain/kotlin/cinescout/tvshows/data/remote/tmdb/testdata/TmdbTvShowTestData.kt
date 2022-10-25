package cinescout.tvshows.data.remote.tmdb.testdata

import cinescout.tvshows.data.remote.tmdb.model.TmdbTvShow
import cinescout.tvshows.domain.testdata.TvShowTestData

object TmdbTvShowTestData {

    val BreakingBad = TmdbTvShow(
        backdropPath = TvShowTestData.BreakingBad.backdropImage.orNull()?.path,
        firstAirDate = TvShowTestData.BreakingBad.firstAirDate,
        id = TvShowTestData.BreakingBad.tmdbId,
        title = TvShowTestData.BreakingBad.title,
        overview = TvShowTestData.BreakingBad.overview,
        posterPath = TvShowTestData.BreakingBad.posterImage.orNull()?.path,
        voteCount = TvShowTestData.BreakingBad.rating.voteCount,
        voteAverage = TvShowTestData.BreakingBad.rating.average.value
    )

    val Dexter = TmdbTvShow(
        backdropPath = TvShowTestData.Dexter.backdropImage.orNull()?.path,
        firstAirDate = TvShowTestData.Dexter.firstAirDate,
        id = TvShowTestData.Dexter.tmdbId,
        title = TvShowTestData.Dexter.title,
        overview = TvShowTestData.Dexter.overview,
        posterPath = TvShowTestData.Dexter.posterImage.orNull()?.path,
        voteCount = TvShowTestData.Dexter.rating.voteCount,
        voteAverage = TvShowTestData.Dexter.rating.average.value
    )

    val Grimm = TmdbTvShow(
        backdropPath = TvShowTestData.Grimm.backdropImage.orNull()?.path,
        firstAirDate = TvShowTestData.Grimm.firstAirDate,
        id = TvShowTestData.Grimm.tmdbId,
        title = TvShowTestData.Grimm.title,
        overview = TvShowTestData.Grimm.overview,
        posterPath = TvShowTestData.Grimm.posterImage.orNull()?.path,
        voteCount = TvShowTestData.Grimm.rating.voteCount,
        voteAverage = TvShowTestData.Grimm.rating.average.value
    )
}
