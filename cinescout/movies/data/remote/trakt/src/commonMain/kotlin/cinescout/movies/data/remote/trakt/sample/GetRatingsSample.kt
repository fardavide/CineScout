package cinescout.movies.data.remote.trakt.sample

import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.screenplay.domain.sample.ScreenplaySample

internal object GetRatingsSample {

    val Inception = GetRatings.Result.Movie(
        movie = GetRatings.Result.MovieBody(
            ids = GetRatings.Result.Ids(
                tmdb = ScreenplaySample.Inception.tmdbId
            ),
            title = ScreenplaySample.Inception.title
        ),
        rating = ScreenplayWithPersonalRatingSample.Inception.personalRating.value
    )
}
