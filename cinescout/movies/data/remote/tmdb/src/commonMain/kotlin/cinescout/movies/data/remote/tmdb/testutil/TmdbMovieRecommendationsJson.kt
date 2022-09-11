package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieRecommendations

object TmdbMovieRecommendationsJson {

    val TwoMovies = """
    {
        "${GetMovieRecommendations.Response.Page}": "1",
        "${GetMovieRecommendations.Response.Results}": [
            ${TmdbMovieJson.Inception},
            ${TmdbMovieJson.TheWolfOfWallStreet}
        ],
        "${GetMovieRecommendations.Response.TotalPages}": "1",
        "${GetMovieRecommendations.Response.TotalResults}": "1"
    }
    """
}
