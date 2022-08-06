package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.DiscoverMovies

object TmdbDiscoverMoviesJson {

    val TwoMovies = """
    {
        "${DiscoverMovies.Response.Page}": "1",
        "${DiscoverMovies.Response.Results}": [
            ${TmdbMovieJson.Inception},
            ${TmdbMovieJson.TheWolfOfWallStreet}
        ],
        "${DiscoverMovies.Response.TotalPages}": "1",
        "${DiscoverMovies.Response.TotalResults}": "1"
    }
    """
}
