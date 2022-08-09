package cinescout.movies.data.remote.trakt.testutil

import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData

object TraktMoviesRatingJson {

    val OneMovie = """
        [
            {
                "movie": {
                    "title": "${MovieTestData.Inception.title}",
                    "ids": {
                        "tmdb": ${MovieTestData.Inception.tmdbId.value}
                    }
                },
                "rating": ${MovieWithPersonalRatingTestData.Inception.rating.value}
            }
        ]
    """
}
