package cinescout.search.presentation.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.search.presentation.model.SearchLikedItemUiModel

object SearchLikedItemUiModelSample {

    val Dexter = SearchLikedItemUiModel(
        screenplayId = ScreenplaySample.Dexter.tmdbId,
        title = ScreenplaySample.Dexter.title
    )

    val Grimm = SearchLikedItemUiModel(
        screenplayId = ScreenplaySample.Grimm.tmdbId,
        title = ScreenplaySample.Grimm.title
    )

    val Inception = SearchLikedItemUiModel(
        screenplayId = ScreenplaySample.Inception.tmdbId,
        title = ScreenplaySample.Inception.title
    )

    val TheWolfOfWallStreet = SearchLikedItemUiModel(
        screenplayId = ScreenplaySample.TheWolfOfWallStreet.tmdbId,
        title = ScreenplaySample.TheWolfOfWallStreet.title
    )
}
