package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import com.soywiz.klock.DateFormat

object TmdbMovieJson {

    val Inception = """
    {
        "id": "${TmdbMovieTestData.Inception.id.value}",
        "release_date": "${TmdbMovieTestData.Inception.releaseDate.format(DateFormat.FORMAT_DATE)}",
        "title": "${TmdbMovieTestData.Inception.title}"
    }
    """

    val TheWolfOfWallStreet = """
    {
        "id": "${TmdbMovieTestData.TheWolfOfWallStreet.id.value}",
        "release_date": "${TmdbMovieTestData.TheWolfOfWallStreet.releaseDate.format(DateFormat.FORMAT_DATE)}",
        "title": "${TmdbMovieTestData.TheWolfOfWallStreet.title}"
    }
    """
}
