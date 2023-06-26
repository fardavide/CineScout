package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.id.ScreenplayIds

data class SearchItemUiModel(
    val screenplayIds: ScreenplayIds,
    val title: String
)
