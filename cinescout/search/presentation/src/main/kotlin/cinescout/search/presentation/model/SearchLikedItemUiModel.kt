package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.id.ScreenplayIds

data class SearchLikedItemUiModel(
    val screenplayIds: ScreenplayIds,
    val title: String
)
