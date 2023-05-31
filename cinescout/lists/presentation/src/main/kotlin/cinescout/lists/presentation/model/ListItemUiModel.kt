package cinescout.lists.presentation.model

import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds

sealed interface ListItemUiModel {
    val ids: ScreenplayIds
    val personalRating: String?
    val rating: String
    val title: String

    data class Movie(
        override val ids: MovieIds,
        override val personalRating: String?,
        override val rating: String,
        override val title: String
    ) : ListItemUiModel

    data class TvShow(
        override val ids: TvShowIds,
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
    is MovieIds -> ListItemUiModel.Movie(
        ids = ids,
        personalRating = personalRating,
        rating = rating,
        title = title
    )
    is TvShowIds -> ListItemUiModel.TvShow(
        ids = ids,
        personalRating = personalRating,
        rating = rating,
        title = title
    )
}
