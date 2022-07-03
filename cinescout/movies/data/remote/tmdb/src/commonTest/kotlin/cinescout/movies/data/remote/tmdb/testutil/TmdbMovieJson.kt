package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.testdata.TmdbMovieTestData

object TmdbMovieJson {

    val Inception = """
        {
            "id": "${TmdbMovieTestData.Inception.id.value}",
            "title": "${TmdbMovieTestData.Inception.title}"
        }
    """
}
