package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.tmdb.model.DiscoverMovies

object DiscoverMoviesResponseTestData {

    val OneMovie = DiscoverMovies.Response(
        page = 1,
        results = listOf(DiscoverMoviesPageResultTestData.Inception),
        totalPages = 1,
        totalResults = 1
    )

    val TwoMovies = DiscoverMovies.Response(
        page = 1,
        results = listOf(
            DiscoverMoviesPageResultTestData.Inception,
            DiscoverMoviesPageResultTestData.TheWolfOfWallStreet
        ),
        totalPages = 1,
        totalResults = 1
    )
}
