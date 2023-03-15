package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.GetTvShowResponse
import cinescout.screenplay.data.remote.tmdb.model.TmdbMovie
import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.data.remote.tmdb.model.TmdbTvShow
import cinescout.screenplay.data.remote.tmdb.sample.TmdbMovieSample
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import com.soywiz.klock.DateFormat
import cinescout.screenplay.data.remote.tmdb.model.GetTvShowResponse as Response

object TmdbMovieDetailsJson {

    val Inception = """
        {
            "${GetTvShowResponse.Genres}": [
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.Inception.genres[0].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.Inception.genres[0].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.Inception.genres[1].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.Inception.genres[1].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.Inception.genres[2].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.Inception.genres[2].name}"
                }
            ],
            "${TmdbScreenplay.Id}": "${TmdbMovieSample.Inception.id.value}",
            "${TmdbTvShow.Name}": "${TmdbMovieSample.Inception.title}",
            "${TmdbTvShow.Overview}": "${TmdbMovieSample.Inception.overview}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.Inception.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbTvShow.VoteAverage}": "${TmdbMovieSample.Inception.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbMovieSample.Inception.voteCount}"
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${GetTvShowResponse.Genres}": [
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.TheWolfOfWallStreet.genres[0].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.TheWolfOfWallStreet.genres[0].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.TheWolfOfWallStreet.genres[1].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.TheWolfOfWallStreet.genres[1].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.TheWolfOfWallStreet.genres[2].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.TheWolfOfWallStreet.genres[2].name}"
                }
            ],
            "${TmdbScreenplay.Id}": "${TmdbMovieSample.TheWolfOfWallStreet.id.value}",
            "${TmdbTvShow.Name}": "${TmdbMovieSample.TheWolfOfWallStreet.title}",
            "${TmdbTvShow.Overview}": "${TmdbMovieSample.TheWolfOfWallStreet.overview}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.TheWolfOfWallStreet.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbTvShow.VoteAverage}": "${TmdbMovieSample.TheWolfOfWallStreet.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbMovieSample.TheWolfOfWallStreet.voteCount}"
        }
    """.trimIndent()

    val War = """
        {
            "${GetTvShowResponse.Genres}": [
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.War.genres[0].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.War.genres[0].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.War.genres[1].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.War.genres[1].name}"
                },
                {
                    "${Response.Genre.Id}": "${ScreenplayGenresSample.War.genres[2].id.value}",
                    "${Response.Genre.Name}": "${ScreenplayGenresSample.War.genres[2].name}"
                }
            ],
            "${TmdbScreenplay.Id}": "${TmdbMovieSample.War.id.value}",
            "${TmdbTvShow.Name}": "${TmdbMovieSample.War.title}",
            "${TmdbTvShow.Overview}": "${TmdbMovieSample.War.overview}",
            "${TmdbMovie.ReleaseDate}": "${TmdbMovieSample.War.releaseDate?.format(DateFormat.FORMAT_DATE)}",
            "${TmdbTvShow.VoteAverage}": "${TmdbMovieSample.War.voteAverage}",
            "${TmdbTvShow.VoteCount}": "${TmdbMovieSample.War.voteCount}"
        }
    """.trimIndent()
}
