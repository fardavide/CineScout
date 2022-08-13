package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieWatchlist

object TmdbMoviesWatchlistJson {

    val OneMovie = """
        {
            "${GetMovieWatchlist.Response.Page}": "1",
            "${GetMovieWatchlist.Response.Results}": [
                ${TmdbMovieJson.Inception} 
            ],
            "${GetMovieWatchlist.Response.TotalPages}": "1",
            "${GetMovieWatchlist.Response.TotalResults}": "1"
        }
    """
}
