package cinescout.movies.data.remote.trakt.testutil

import cinescout.movies.domain.testdata.MovieTestData

object TraktMoviesWatchlistJson {

    val OneMovie = """
        [
            {
                "movie": {
                    "title": "${MovieTestData.Inception.title}",
                    "ids": {
                        "tmdb": ${MovieTestData.Inception.tmdbId.value}
                    }
                }
            }
        ]
    """
}
