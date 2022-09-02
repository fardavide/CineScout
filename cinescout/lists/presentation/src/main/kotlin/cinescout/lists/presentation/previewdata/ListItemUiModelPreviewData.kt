package cinescout.lists.presentation.previewdata

import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.TmdbPosterImage
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData

object ListItemUiModelPreviewData {

    val Inception = ListItemUiModel(
        personalRating = MovieWithPersonalRatingTestData.Inception.personalRatingString(),
        posterUrl = MovieTestData.Inception.posterUrl(),
        rating = MovieTestData.Inception.ratingString(),
        title = MovieTestData.Inception.title,
        tmdbId = MovieTestData.Inception.tmdbId
    )

    val TheWolfOfWallStreet = ListItemUiModel(
        personalRating = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.personalRatingString(),
        posterUrl = MovieTestData.TheWolfOfWallStreet.posterUrl(),
        rating = MovieTestData.TheWolfOfWallStreet.ratingString(),
        title = MovieTestData.TheWolfOfWallStreet.title,
        tmdbId = MovieTestData.TheWolfOfWallStreet.tmdbId
    )

    private fun MovieWithPersonalRating.personalRatingString() = personalRating.value.toString()
    private fun Movie.posterUrl() = posterImage.orNull()?.getUrl(TmdbPosterImage.Size.MEDIUM)
    private fun Movie.ratingString() = rating.average.value.toString()
}
