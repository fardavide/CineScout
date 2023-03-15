package cinescout.search.presentation.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.search.presentation.model.SearchLikedItemUiModel

object SearchLikedItemUiModelSample {

    val Dexter = SearchLikedItemUiModel(
        tvShowId = ScreenplaySample.Dexter.tmdbId,
        title = ScreenplaySample.Dexter.title
    )

    val Grimm = SearchLikedItemUiModel(
        tvShowId = ScreenplaySample.Grimm.tmdbId,
        title = ScreenplaySample.Grimm.title
    )

    val Inception = SearchLikedItemUiModel(
        movieId = ScreenplaySample.Inception.tmdbId,
        title = ScreenplaySample.Inception.title
    )

    val TheWolfOfWallStreet = SearchLikedItemUiModel(
        movieId = ScreenplaySample.TheWolfOfWallStreet.tmdbId,
        title = ScreenplaySample.TheWolfOfWallStreet.title
    )
}
