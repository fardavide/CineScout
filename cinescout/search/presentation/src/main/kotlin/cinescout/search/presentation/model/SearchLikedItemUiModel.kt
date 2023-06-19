package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.ids.ScreenplayIds

data class SearchLikedItemUiModel(
    val screenplayIds: ScreenplayIds,
    val title: String
)
