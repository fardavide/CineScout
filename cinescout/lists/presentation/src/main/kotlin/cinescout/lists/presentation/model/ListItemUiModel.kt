package cinescout.lists.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ListItemUiModel {
    val personalRating: String?
    val rating: String
    val title: String
    val tmdbId: TmdbScreenplayId

    data class Movie(
        override val personalRating: String?,
        override val rating: String,
        override val title: String,
        override val tmdbId: TmdbScreenplayId.Movie
    ) : ListItemUiModel

    data class TvShow(
        override val personalRating: String?,
        override val rating: String,
        override val title: String,
        override val tmdbId: TmdbScreenplayId.TvShow
    ) : ListItemUiModel
}

fun ListItemUiModel(
    personalRating: String?,
    rating: String,
    title: String,
    tmdbId: TmdbScreenplayId
): ListItemUiModel = when (tmdbId) {
    is TmdbScreenplayId.Movie -> ListItemUiModel.Movie(
        personalRating = personalRating,
        rating = rating,
        title = title,
        tmdbId = tmdbId
    )
    is TmdbScreenplayId.TvShow -> ListItemUiModel.TvShow(
        personalRating = personalRating,
        rating = rating,
        title = title,
        tmdbId = tmdbId
    )
}
