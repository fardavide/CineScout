package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.ids.TmdbScreenplayId

data class SearchLikedItemUiModel(
    val screenplayId: TmdbScreenplayId,
    val title: String
)
