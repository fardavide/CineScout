package cinescout.search.presentation.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.search.presentation.model.SearchLikedItemUiModel

object SearchLikedItemUiModelSample {

    val Dexter = SearchLikedItemUiModel(
        screenplayIds = ScreenplaySample.Dexter.ids,
        title = ScreenplaySample.Dexter.title
    )

    val Grimm = SearchLikedItemUiModel(
        screenplayIds = ScreenplaySample.Grimm.ids,
        title = ScreenplaySample.Grimm.title
    )

    val Inception = SearchLikedItemUiModel(
        screenplayIds = ScreenplaySample.Inception.ids,
        title = ScreenplaySample.Inception.title
    )

    val TheWolfOfWallStreet = SearchLikedItemUiModel(
        screenplayIds = ScreenplaySample.TheWolfOfWallStreet.ids,
        title = ScreenplaySample.TheWolfOfWallStreet.title
    )
}
