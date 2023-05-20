package cinescout.lists.presentation.mapper

import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListOptionUiModel
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.settings.domain.model.SavedListOptions
import org.koin.core.annotation.Factory

@Factory
internal class SavedListOptionsMapper {

    fun toUiModel(savedListOptions: Option<SavedListOptions>): Option<ListOptionUiModel> {
        return savedListOptions.map { options ->
            ListOptionUiModel(
                listFilter = toListFilter(options.filter),
                listSorting = toListSorting(options.sorting),
                screenplayTypeFilter = toScreenplayTypeFilter(options.type)
            )
        }
    }

    private fun toListFilter(savedFilter: SavedListOptions.Filter): ListFilter = when (savedFilter) {
        SavedListOptions.Filter.Disliked -> ListFilter.Disliked
        SavedListOptions.Filter.Liked -> ListFilter.Liked
        SavedListOptions.Filter.Rated -> ListFilter.Rated
        SavedListOptions.Filter.Watchlist -> ListFilter.Watchlist
    }

    private fun toListSorting(savedSorting: SavedListOptions.Sorting): ListSorting = when (savedSorting) {
        SavedListOptions.Sorting.RatingAscending -> ListSorting.Rating.Ascending
        SavedListOptions.Sorting.RatingDescending -> ListSorting.Rating.Descending
        SavedListOptions.Sorting.ReleaseDateAscending -> ListSorting.ReleaseDate.Ascending
        SavedListOptions.Sorting.ReleaseDateDescending -> ListSorting.ReleaseDate.Descending
    }

    private fun toScreenplayTypeFilter(savedType: SavedListOptions.Type): ScreenplayTypeFilter =
        when (savedType) {
            SavedListOptions.Type.All -> ScreenplayTypeFilter.All
            SavedListOptions.Type.Movies -> ScreenplayTypeFilter.Movies
            SavedListOptions.Type.TvShows -> ScreenplayTypeFilter.TvShows
        }
}


