package cinescout.search.presentation.model

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId

interface SearchLikedItemUiModel {

    val itemId: SearchLikedItemId
    val posterUrl: String?
    val title: String

    data class Movie(
        override val itemId: SearchLikedItemId.Movie,
        override val posterUrl: String?,
        override val title: String
    ) : SearchLikedItemUiModel

    data class TvShow(
        override val itemId: SearchLikedItemId.TvShow,
        override val posterUrl: String?,
        override val title: String
    ) : SearchLikedItemUiModel
}

fun SearchLikedItemUiModel(
    movieId: TmdbMovieId,
    posterUrl: String?,
    title: String
) = SearchLikedItemUiModel.Movie(
    itemId = SearchLikedItemId.Movie(movieId),
    posterUrl = posterUrl,
    title = title
)

fun SearchLikedItemUiModel(
    tvShowId: TmdbTvShowId,
    posterUrl: String?,
    title: String
) = SearchLikedItemUiModel.TvShow(
    itemId = SearchLikedItemId.TvShow(tvShowId),
    posterUrl = posterUrl,
    title = title
)
