package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds

data class SearchItemUiModel(
    val screenplayIds: ScreenplayIds,
    val title: String
)
