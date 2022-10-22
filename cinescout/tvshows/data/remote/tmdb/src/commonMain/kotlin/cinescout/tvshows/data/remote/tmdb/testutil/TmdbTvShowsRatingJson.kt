package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.GetRatedTvShows

object TmdbTvShowsRatingJson {

    val OneTvShow = """
        {
            "${GetRatedTvShows.Response.Page}": "1",
            "${GetRatedTvShows.Response.Results}": [
                ${TmdbTvShowJson.Grimm.withRating(9.0)} 
            ],
            "${GetRatedTvShows.Response.TotalPages}": "1",
            "${GetRatedTvShows.Response.TotalResults}": "1"
        }
    """
}

private fun String.withRating(rating: Double) =
    substringBeforeLast("}")
        .plus(",\n    \"${GetRatedTvShows.Response.PageResult.Rating}\": \"$rating\"\n}")
