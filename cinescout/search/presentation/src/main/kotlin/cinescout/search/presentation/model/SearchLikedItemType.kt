package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.ScreenplayType

enum class SearchLikedItemType {
    Movies,
    TvShows
}

fun SearchLikedItemType.toScreenplayType(): ScreenplayType = when (this) {
    SearchLikedItemType.Movies -> ScreenplayType.Movies
    SearchLikedItemType.TvShows -> ScreenplayType.TvShows
}
