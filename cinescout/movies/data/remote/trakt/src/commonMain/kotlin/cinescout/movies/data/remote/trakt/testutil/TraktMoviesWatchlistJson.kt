package cinescout.movies.data.remote.trakt.testutil

import cinescout.movies.domain.sample.MovieSample

object TraktMoviesWatchlistJson {

    val OneMovie = """
        [
            {
                "movie": {
                    "title": "${MovieSample.Inception.title}",
                    "ids": {
                        "tmdb": ${MovieSample.Inception.tmdbId.value}
                    }
                }
            }
        ]
    """.trimIndent()
}
