package cinescout.movies.data.remote.trakt.testutil

import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample

object TraktMoviesRatingJson {

    val OneMovie = """
        [
            {
                "${GetRatings.Result.MovieType}": {
                    "${GetRatings.Result.Title}": "${MovieSample.Inception.title}",
                    "${GetRatings.Result.Ids}": {
                        "${GetRatings.Result.Ids.Tmdb}": ${MovieSample.Inception.tmdbId.value}
                    }
                },
                "${GetRatings.Result.Rating}": ${MovieWithPersonalRatingSample.Inception.personalRating.value}
            }
        ]
    """.trimIndent()
}
