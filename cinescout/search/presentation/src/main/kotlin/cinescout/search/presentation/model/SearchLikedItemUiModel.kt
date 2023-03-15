package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

interface SearchLikedItemUiModel {

    val itemId: TmdbScreenplayId
    val title: String

    data class Movie(
        override val itemId: TmdbScreenplayId.Movie,
        override val title: String
    ) : SearchLikedItemUiModel

    data class TvShow(
        override val itemId: TmdbScreenplayId.TvShow,
        override val title: String
    ) : SearchLikedItemUiModel
}

fun SearchLikedItemUiModel(screenplayId: TmdbScreenplayId, title: String): SearchLikedItemUiModel =
    when (screenplayId) {
        is TmdbScreenplayId.Movie -> SearchLikedItemUiModel.Movie(
            itemId = screenplayId,
            title = title
        )
        is TmdbScreenplayId.TvShow -> SearchLikedItemUiModel.TvShow(
            itemId = screenplayId,
            title = title
        )
    }

fun SearchLikedItemUiModel(movieId: TmdbScreenplayId.Movie, title: String) = SearchLikedItemUiModel.Movie(
    itemId = movieId,
    title = title
)

fun SearchLikedItemUiModel(tvShowId: TmdbScreenplayId.TvShow, title: String) = SearchLikedItemUiModel.TvShow(
    itemId = tvShowId,
    title = title
)
