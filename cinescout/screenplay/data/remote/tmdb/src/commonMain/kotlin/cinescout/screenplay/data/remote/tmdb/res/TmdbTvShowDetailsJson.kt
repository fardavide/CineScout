package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.GetTvShowResponse
import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.data.remote.tmdb.sample.TmdbTvShowSample
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import korlibs.time.DateFormat
import cinescout.screenplay.data.remote.tmdb.model.GetTvShowResponse as Response

object TmdbTvShowDetailsJson {

    val BreakingBad = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.BreakingBad.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${GetTvShowResponse.Genres}": [
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.BreakingBad.genres[0].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.BreakingBad.genres[0].name}"
                }
            ],
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.BreakingBad.id.value}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.BreakingBad.title}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.BreakingBad.overview}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.BreakingBad.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.BreakingBad.voteCount}"
        }
    """.trimIndent()

    val Dexter = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.Dexter.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${GetTvShowResponse.Genres}": [
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.Dexter.genres[0].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.Dexter.genres[0].name}"
                }
            ],
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.Dexter.id.value}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.Dexter.title}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.Dexter.overview}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.Dexter.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.Dexter.voteCount}"
        }
    """.trimIndent()

    val Grimm = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.Grimm.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${GetTvShowResponse.Genres}": [
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.Grimm.genres[0].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.Grimm.genres[0].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.Grimm.genres[1].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.Grimm.genres[1].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.Grimm.genres[2].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.Grimm.genres[2].name}"
                }
            ],
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.Grimm.id.value}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.Grimm.title}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.Grimm.overview}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.Grimm.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.Grimm.voteCount}"
        }
    """.trimIndent()

    val TheWalkingDeadDeadCity = """
        {
            "${TmdbTvShow.FirstAirDate}": "${TmdbTvShowSample.TheWalkingDeadDeadCity.firstAirDate.format(DateFormat.FORMAT_DATE)}",
            "${GetTvShowResponse.Genres}": [
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.TheWalkingDeadDeadCity.genres[0].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.TheWalkingDeadDeadCity.genres[0].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.TheWalkingDeadDeadCity.genres[1].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.TheWalkingDeadDeadCity.genres[1].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.TheWalkingDeadDeadCity.genres[2].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.TheWalkingDeadDeadCity.genres[2].name}"
                }
            ],
            "${TmdbScreenplay.Id}": "${TmdbTvShowSample.TheWalkingDeadDeadCity.id.value}",
            "${TmdbTvShow.Name}": "${TmdbTvShowSample.TheWalkingDeadDeadCity.title}",
            "${TmdbTvShow.Overview}": "${TmdbTvShowSample.TheWalkingDeadDeadCity.overview}",
            "${TmdbTvShow.VoteAverage}": "${TmdbTvShowSample.TheWalkingDeadDeadCity.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbTvShowSample.TheWalkingDeadDeadCity.voteCount}"
        }
    """.trimIndent()
}
