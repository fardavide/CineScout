package cinescout.lists.presentation.previewdata

import cinescout.common.model.TmdbPosterImage
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample

object ListItemUiModelPreviewData {

    val Inception = ListItemUiModel.Movie(
        personalRating = MovieWithPersonalRatingSample.Inception.personalRatingString(),
        posterUrl = MovieSample.Inception.posterUrl(),
        rating = MovieSample.Inception.ratingString(),
        title = MovieSample.Inception.title,
        tmdbId = MovieSample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = ListItemUiModel.Movie(
        personalRating = MovieWithPersonalRatingSample.TheWolfOfWallStreet.personalRatingString(),
        posterUrl = MovieSample.TheWolfOfWallStreet.posterUrl(),
        rating = MovieSample.TheWolfOfWallStreet.ratingString(),
        title = MovieSample.TheWolfOfWallStreet.title,
        tmdbId = MovieSample.TheWolfOfWallStreet.tmdbId
    )

    private fun MovieWithPersonalRating.personalRatingString() = personalRating.value.toString()
    private fun Movie.posterUrl() = posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM)
    private fun Movie.ratingString() = rating.average.value.toString()
}
