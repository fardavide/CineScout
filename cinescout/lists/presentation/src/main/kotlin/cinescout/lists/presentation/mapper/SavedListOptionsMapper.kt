package cinescout.lists.presentation.mapper

import arrow.core.Option
import arrow.core.none
import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.SortingDirection
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListOptionUiModel
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.settings.domain.model.SavedListOptions
import org.koin.core.annotation.Factory

@Factory
internal class SavedListOptionsMapper {
    
    fun toDomainModel(uiModel: ListOptionUiModel) = SavedListOptions(
        filter = toFilter(uiModel.listFilter),
        sorting = toSorting(uiModel.listSorting),
        type = toType(uiModel.screenplayTypeFilter)
    )

    fun toUiModel(savedListOptions: Option<SavedListOptions>): Option<ListOptionUiModel> =
        savedListOptions.map { options ->
            ListOptionUiModel(
                genreFilter = none(),
                listFilter = toListFilter(options.filter),
                listSorting = toListSorting(options.sorting),
                screenplayTypeFilter = toScreenplayTypeFilter(options.type)
            )
        }
    
    private fun toFilter(listFilter: ListFilter): SavedListOptions.Filter = when (listFilter) {
        ListFilter.Disliked -> SavedListOptions.Filter.Disliked
        ListFilter.InProgress -> SavedListOptions.Filter.InProgress
        ListFilter.Liked -> SavedListOptions.Filter.Liked
        ListFilter.Rated -> SavedListOptions.Filter.Rated
        ListFilter.Watchlist -> SavedListOptions.Filter.Watchlist
    }
    
    private fun toSorting(listSorting: ListSorting): SavedListOptions.Sorting = when (listSorting) {
        is ListSorting.Rating -> when (listSorting.direction) {
            SortingDirection.Ascending -> SavedListOptions.Sorting.RatingAscending
            SortingDirection.Descending -> SavedListOptions.Sorting.RatingDescending
        }
        is ListSorting.ReleaseDate -> when (listSorting.direction) {
            SortingDirection.Ascending -> SavedListOptions.Sorting.ReleaseDateAscending
            SortingDirection.Descending -> SavedListOptions.Sorting.ReleaseDateDescending
        }
    }
    
    private fun toType(screenplayTypeFilter: ScreenplayTypeFilter): SavedListOptions.Type =
        when (screenplayTypeFilter) {
            ScreenplayTypeFilter.All -> SavedListOptions.Type.All
            ScreenplayTypeFilter.Movies -> SavedListOptions.Type.Movies
            ScreenplayTypeFilter.TvShows -> SavedListOptions.Type.TvShows
        }

    private fun toListFilter(savedFilter: SavedListOptions.Filter): ListFilter = when (savedFilter) {
        SavedListOptions.Filter.Disliked -> ListFilter.Disliked
        SavedListOptions.Filter.InProgress -> ListFilter.InProgress
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


