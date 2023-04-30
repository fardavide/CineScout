package cinescout.search.presentation.model

import cinescout.screenplay.domain.model.ScreenplayTypeFilter

enum class SearchLikedItemType {
    Movies,
    TvShows
}

fun SearchLikedItemType.toScreenplayType(): ScreenplayTypeFilter = when (this) {
    SearchLikedItemType.Movies -> ScreenplayTypeFilter.Movies
    SearchLikedItemType.TvShows -> ScreenplayTypeFilter.TvShows
}
