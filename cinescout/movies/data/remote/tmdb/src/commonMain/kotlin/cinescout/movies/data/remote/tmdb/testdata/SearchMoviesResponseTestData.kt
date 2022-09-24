package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.tmdb.model.SearchMovie

object SearchMoviesResponseTestData {

    val OneMovie = SearchMovie.Response(
        page = 1,
        results = listOf(SearchMoviePageResultTestData.Inception),
        totalPages = 1,
        totalResults = 1
    )
}
