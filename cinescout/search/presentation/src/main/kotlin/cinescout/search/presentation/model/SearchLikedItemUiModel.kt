package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

interface SearchLikedItemUiModel {

    val itemId: SearchLikedItemId
    val title: String

    data class Movie(
        override val itemId: SearchLikedItemId.Movie,
        override val title: String
    ) : SearchLikedItemUiModel

    data class TvShow(
        override val itemId: SearchLikedItemId.TvShow,
        override val title: String
    ) : SearchLikedItemUiModel
}

fun SearchLikedItemUiModel(screenplayId: TmdbScreenplayId, title: String): SearchLikedItemUiModel =
    when (screenplayId) {
        is TmdbScreenplayId.Movie -> SearchLikedItemUiModel.Movie(
            itemId = SearchLikedItemId.Movie(screenplayId),
            title = title
        )
        is TmdbScreenplayId.TvShow -> SearchLikedItemUiModel.TvShow(
            itemId = SearchLikedItemId.TvShow(screenplayId),
            title = title
        )
    }

fun SearchLikedItemUiModel(movieId: TmdbScreenplayId.Movie, title: String) = SearchLikedItemUiModel.Movie(
    itemId = SearchLikedItemId.Movie(movieId),
    title = title
)

fun SearchLikedItemUiModel(tvShowId: TmdbScreenplayId.TvShow, title: String) = SearchLikedItemUiModel.TvShow(
    itemId = SearchLikedItemId.TvShow(tvShowId),
    title = title
)
