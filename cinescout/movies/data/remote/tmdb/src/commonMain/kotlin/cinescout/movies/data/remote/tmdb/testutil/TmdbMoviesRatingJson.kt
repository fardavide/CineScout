package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetRatedMovies

object TmdbMoviesRatingJson {

    val OneMovie = """
        {
            "${GetRatedMovies.Response.Page}": "1",
            "${GetRatedMovies.Response.Results}": [
                ${TmdbMovieJson.Inception.withRating(9.0)} 
            ],
            "${GetRatedMovies.Response.TotalPages}": "1",
            "${GetRatedMovies.Response.TotalResults}": "1"
        }
    """
}

private fun String.withRating(rating: Double) =
    substringBefore("}")
        .plus(",\n    \"${GetRatedMovies.Response.PageResult.Rating}\": \"$rating\"\n}")
