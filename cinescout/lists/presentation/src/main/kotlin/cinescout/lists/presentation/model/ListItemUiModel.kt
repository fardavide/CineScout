package cinescout.lists.presentation.model

import cinescout.screenplay.domain.model.ScreenplayIds

sealed interface ListItemUiModel {
    val ids: ScreenplayIds
    val personalRating: String?
    val rating: String
    val title: String

    data class Movie(
        override val ids: ScreenplayIds.Movie,
        override val personalRating: String?,
        override val rating: String,
        override val title: String
    ) : ListItemUiModel

    data class TvShow(
        override val ids: ScreenplayIds.TvShow,
        override val personalRating: String?,
        override val rating: String,
        override val title: String
    ) : ListItemUiModel
}

fun ListItemUiModel(
    ids: ScreenplayIds,
    personalRating: String?,
    rating: String,
    title: String
): ListItemUiModel = when (ids) {
    is ScreenplayIds.Movie -> ListItemUiModel.Movie(
        ids = ids,
        personalRating = personalRating,
        rating = rating,
        title = title
    )
    is ScreenplayIds.TvShow -> ListItemUiModel.TvShow(
        ids = ids,
        personalRating = personalRating,
        rating = rating,
        title = title
    )
}
