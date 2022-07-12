package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.tmdb.model.GetRatedMovies

object GetRatedMoviesResponseTestData {

    val OneMovie = GetRatedMovies.Response(
        page = 1,
        results = listOf(GetRatedMoviesPageResultTestData.Inception),
        totalPages = 1,
        totalResults = 1
    )
}