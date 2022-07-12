package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.testdata.TmdbMovieTestData

object TmdbMoviesRatingJson {

    val OneMovie = """
    {
        "page": "1",
        "results": [
            {
                "id": "${TmdbMovieTestData.Inception.id.value}"
                "title": "${TmdbMovieTestData.Inception.title}",
                "rating": "9"
            }
        ],
        "total_pages": "1",
        "total_results": "1"
    }
    """
}
