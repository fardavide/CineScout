package cinescout.lists.presentation.sample

import arrow.core.Nel
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.sample.ScreenplayIdWithPersonalRatingSample
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.sample.ScreenplaySample

object ListItemUiModelSample {

    val BreakingBad = ListItemUiModel.TvShow(
        personalRating = ScreenplayIdWithPersonalRatingSample.BreakingBad.personalRatingString(),
        rating = ScreenplaySample.BreakingBad.ratingString(),
        title = ScreenplaySample.BreakingBad.title,
        tmdbId = ScreenplaySample.BreakingBad.tmdbId
    )

    val Dexter = ListItemUiModel.TvShow(
        personalRating = ScreenplayIdWithPersonalRatingSample.Dexter.personalRatingString(),
        rating = ScreenplaySample.Dexter.ratingString(),
        title = ScreenplaySample.Dexter.title,
        tmdbId = ScreenplaySample.Dexter.tmdbId
    )

    val Inception = ListItemUiModel.Movie(
        personalRating = ScreenplayIdWithPersonalRatingSample.Inception.personalRatingString(),
        rating = ScreenplaySample.Inception.ratingString(),
        title = ScreenplaySample.Inception.title,
        tmdbId = ScreenplaySample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = ListItemUiModel.Movie(
        personalRating = ScreenplayIdWithPersonalRatingSample.TheWolfOfWallStreet.personalRatingString(),
        rating = ScreenplaySample.TheWolfOfWallStreet.ratingString(),
        title = ScreenplaySample.TheWolfOfWallStreet.title,
        tmdbId = ScreenplaySample.TheWolfOfWallStreet.tmdbId
    )

    private fun ListItemUiModel.withoutPersonalRating() = when (this) {
        is ListItemUiModel.Movie -> copy(personalRating = null)
        is ListItemUiModel.TvShow -> copy(personalRating = null)
    }

    fun Nel<ListItemUiModel>.withoutPersonalRating() = map { it.withoutPersonalRating() }

    private fun ScreenplayIdWithPersonalRating.personalRatingString() = personalRating.value.toString()
    private fun Screenplay.ratingString() = rating.average.value.toString()
}
