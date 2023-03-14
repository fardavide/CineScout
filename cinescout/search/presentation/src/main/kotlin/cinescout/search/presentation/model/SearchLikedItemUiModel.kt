package cinescout.search.presentation.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.tvshows.domain.model.TmdbTvShowId

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
        is TmdbMovieId -> SearchLikedItemUiModel.Movie(
            itemId = SearchLikedItemId.Movie(screenplayId),
            title = title
        )
        is TmdbTvShowId -> SearchLikedItemUiModel.TvShow(
            itemId = SearchLikedItemId.TvShow(screenplayId),
            title = title
        )
    }

fun SearchLikedItemUiModel(movieId: TmdbMovieId, title: String) = SearchLikedItemUiModel.Movie(
    itemId = SearchLikedItemId.Movie(movieId),
    title = title
)

fun SearchLikedItemUiModel(tvShowId: TmdbTvShowId, title: String) = SearchLikedItemUiModel.TvShow(
    itemId = SearchLikedItemId.TvShow(tvShowId),
    title = title
)
