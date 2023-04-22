package cinescout.search.presentation.sample

import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.search.presentation.model.SearchItemUiModel

object SearchItemUiModelSample {

    val BreakingBad = SearchItemUiModel(
        screenplayIds = ScreenplayIdsSample.BreakingBad,
        title = "Breaking Bad"
    )

    val Dexter = SearchItemUiModel(
        screenplayIds = ScreenplayIdsSample.Dexter,
        title = "Dexter"
    )

    val Grimm = SearchItemUiModel(
        screenplayIds = ScreenplayIdsSample.Grimm,
        title = "Grimm"
    )

    val Inception = SearchItemUiModel(
        screenplayIds = ScreenplayIdsSample.Inception,
        title = "Inception"
    )

    val TheWolfOfWallStreet = SearchItemUiModel(
        screenplayIds = ScreenplayIdsSample.TheWolfOfWallStreet,
        title = "The Wolf of Wall Street"
    )
}
