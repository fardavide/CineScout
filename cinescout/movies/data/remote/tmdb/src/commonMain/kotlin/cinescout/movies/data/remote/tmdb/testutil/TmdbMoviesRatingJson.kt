package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.domain.testdata.MovieTestData
import com.soywiz.klock.DateFormat

object TmdbMoviesRatingJson {

    val OneMovie = """
        {
            "page": "1",
            "results": [
                {
                    "id": "${MovieTestData.Inception.tmdbId.value}"
                    "rating": "9",
                    "release_date": "${MovieTestData.Inception.releaseDate.format(DateFormat.FORMAT_DATE)}"
                    "title": "${MovieTestData.Inception.title}"
                }
            ],
            "total_pages": "1",
            "total_results": "1"
        }
    """
}
