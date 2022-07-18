package cinescout.movies.data.remote.trakt.testdata

import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData

internal object GetRatingsTestData {

    val Inception = GetRatings.Result.Movie(
        movie = GetRatings.Result.MovieBody(
            ids = GetRatings.Result.Ids(
                tmdb = MovieTestData.Inception.tmdbId
            ),
            title = MovieTestData.Inception.title
        ),
        rating = MovieWithRatingTestData.Inception.rating.value.toDouble()
    )
}