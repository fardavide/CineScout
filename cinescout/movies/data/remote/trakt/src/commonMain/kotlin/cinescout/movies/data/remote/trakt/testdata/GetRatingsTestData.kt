package cinescout.movies.data.remote.trakt.testdata

import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData

internal object GetRatingsTestData {

    val Inception = GetRatings.Result.Movie(
        movie = GetRatings.Result.MovieBody(
            ids = GetRatings.Result.Ids(
                tmdb = MovieSample.Inception.tmdbId
            ),
            title = MovieSample.Inception.title
        ),
        rating = MovieWithPersonalRatingTestData.Inception.personalRating.value.toDouble()
    )
}
