package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.TmdbTvShow
import cinescout.tvshows.data.remote.tmdb.testdata.TmdbTvShowTestData
import com.soywiz.klock.DateFormat

object TmdbTvShowJson {

    val Grimm = """
    {
        "${TmdbTvShow.BackdropPath}": "${TmdbTvShowTestData.Grimm.backdropPath}",
        "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowTestData.Grimm.firstAirDate.format(DateFormat.FORMAT_DATE)}",
        "${TmdbTvShow.Id}": "${TmdbTvShowTestData.Grimm.id.value}",
        "${TmdbTvShow.Overview}": "${TmdbTvShowTestData.Grimm.overview}",
        "${TmdbTvShow.Name}": "${TmdbTvShowTestData.Grimm.name}",
        "${TmdbTvShow.PosterPath}": "${TmdbTvShowTestData.Grimm.posterPath}",
        "${TmdbTvShow.VoteAverage}": "${TmdbTvShowTestData.Grimm.voteAverage}",
        "${TmdbTvShow.VoteCount}": "${TmdbTvShowTestData.Grimm.voteCount}"
    }
    """
}
