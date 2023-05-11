package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.data.remote.tmdb.sample.TmdbTvShowSample
import korlibs.time.DateFormat

object TmdbTvShowJson {

    val BreakingBad = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.BreakingBad.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.BreakingBad.id.value}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.BreakingBad.overview}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.BreakingBad.title}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.BreakingBad.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.BreakingBad.voteCount}"
        }
    """.trimIndent()

    val Grimm = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.Grimm.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.Grimm.id.value}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.Grimm.overview}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.Grimm.title}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.Grimm.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.Grimm.voteCount}"
        }
    """.trimIndent()
}
