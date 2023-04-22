package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

data class SearchLikedItemUiModel(
    val screenplayId: TmdbScreenplayId,
    val title: String
)
