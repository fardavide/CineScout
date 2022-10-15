package cinescout.tvshows.data.remote.tmdb.testdata

import cinescout.tvshows.data.remote.tmdb.model.TmdbTvShow
import cinescout.tvshows.domain.testdata.TvShowTestData

object TmdbTvShowTestData {

    val Grimm = TmdbTvShow(
        backdropPath = TvShowTestData.Grimm.backdropImage.orNull()?.path,
        firstAirDate = TvShowTestData.Grimm.firstAirDate,
        id = TvShowTestData.Grimm.tmdbId,
        name = TvShowTestData.Grimm.title,
        overview = TvShowTestData.Grimm.overview,
        posterPath = TvShowTestData.Grimm.posterImage.orNull()?.path,
        voteCount = TvShowTestData.Grimm.rating.voteCount,
        voteAverage = TvShowTestData.Grimm.rating.average.value
    )
}
