package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import com.soywiz.klock.DateFormat

object TmdbDiscoverMoviesJson {

    val OneMovie = """
    {
        "page": "1",
        "results": [
            {
                "id": "${TmdbMovieTestData.Inception.id.value}"
                "release_date": "${TmdbMovieTestData.Inception.releaseDate.format(DateFormat.FORMAT_DATE)}"
                "title": "${TmdbMovieTestData.Inception.title}"
            }
        ],
        "total_pages": "1",
        "total_results": "1"
    }
    """
}
