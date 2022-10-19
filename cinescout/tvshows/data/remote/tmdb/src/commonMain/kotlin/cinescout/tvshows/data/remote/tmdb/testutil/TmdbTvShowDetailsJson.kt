package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.GetTvShowDetails
import cinescout.tvshows.data.remote.tmdb.model.TmdbTvShow
import cinescout.tvshows.data.remote.tmdb.testdata.TmdbTvShowTestData
import cinescout.tvshows.domain.testdata.TvShowWithDetailsTestData
import com.soywiz.klock.DateFormat

object TmdbTvShowDetailsJson {

    val BreakingBad = """
    {
        "${TmdbTvShow.BackdropPath}": "${TmdbTvShowTestData.BreakingBad.backdropPath}",
        "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowTestData.BreakingBad.firstAirDate.format(DateFormat.FORMAT_DATE)}",
        "${GetTvShowDetails.Response.Genres}": [
            {
                "${GetTvShowDetails.Response.Genre.Id}": "${TvShowWithDetailsTestData.BreakingBad.genres[0].id.value}",
                "${GetTvShowDetails.Response.Genre.Name}": "${TvShowWithDetailsTestData.BreakingBad.genres[0].name}"
            }
        ],
        "${TmdbTvShow.Id}": "${TmdbTvShowTestData.BreakingBad.id.value}",
        "${TmdbTvShow.Name}": "${TmdbTvShowTestData.BreakingBad.name}",
        "${TmdbTvShow.Overview}": "${TmdbTvShowTestData.BreakingBad.overview}",
        "${TmdbTvShow.PosterPath}": "${TmdbTvShowTestData.BreakingBad.posterPath}",
        "${TmdbTvShow.VoteAverage}": "${TmdbTvShowTestData.BreakingBad.voteAverage}",
        "${TmdbTvShow.VoteCount}": "${TmdbTvShowTestData.BreakingBad.voteCount}"
    }
    """

    val Grimm = """
    {
        "${TmdbTvShow.BackdropPath}": "${TmdbTvShowTestData.Grimm.backdropPath}",
        "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowTestData.Grimm.firstAirDate.format(DateFormat.FORMAT_DATE)}",
        "${GetTvShowDetails.Response.Genres}": [
            {
                "${GetTvShowDetails.Response.Genre.Id}": "${TvShowWithDetailsTestData.Grimm.genres[0].id.value}",
                "${GetTvShowDetails.Response.Genre.Name}": "${TvShowWithDetailsTestData.Grimm.genres[0].name}"
            },
            {
                "${GetTvShowDetails.Response.Genre.Id}": "${TvShowWithDetailsTestData.Grimm.genres[1].id.value}",
                "${GetTvShowDetails.Response.Genre.Name}": "${TvShowWithDetailsTestData.Grimm.genres[1].name}"
            },
            {
                "${GetTvShowDetails.Response.Genre.Id}": "${TvShowWithDetailsTestData.Grimm.genres[2].id.value}",
                "${GetTvShowDetails.Response.Genre.Name}": "${TvShowWithDetailsTestData.Grimm.genres[2].name}"
            }
        ],
        "${TmdbTvShow.Id}": "${TmdbTvShowTestData.Grimm.id.value}",
        "${TmdbTvShow.Name}": "${TmdbTvShowTestData.Grimm.name}",
        "${TmdbTvShow.Overview}": "${TmdbTvShowTestData.Grimm.overview}",
        "${TmdbTvShow.PosterPath}": "${TmdbTvShowTestData.Grimm.posterPath}",
        "${TmdbTvShow.VoteAverage}": "${TmdbTvShowTestData.Grimm.voteAverage}",
        "${TmdbTvShow.VoteCount}": "${TmdbTvShowTestData.Grimm.voteCount}"
    }
    """
}
