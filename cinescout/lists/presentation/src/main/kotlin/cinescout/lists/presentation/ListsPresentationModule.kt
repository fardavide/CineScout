package cinescout.lists.presentation

import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.viewmodel.DislikedListViewModel
import cinescout.lists.presentation.viewmodel.LikedListViewModel
import cinescout.lists.presentation.viewmodel.RatedListViewModel
import cinescout.lists.presentation.viewmodel.WatchlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ListsPresentationModule = module {

    viewModel {
        DislikedListViewModel(
            getAllDislikedMovies = get(),
            listItemUiModelMapper = get()
        )
    }
    viewModel {
        LikedListViewModel(
            getAllLikedMovies = get(),
            listItemUiModelMapper = get()
        )
    }
    factory { ListItemUiModelMapper() }
    viewModel {
        RatedListViewModel(
            errorToMessageMapper = get(),
            getAllRatedMovies = get(),
            getAllRatedTvShows = get(),
            listItemUiModelMapper = get()
        )
    }
    viewModel {
        WatchlistViewModel(
            errorToMessageMapper = get(),
            getAllWatchlistMovies = get(),
            getAllWatchlistTvShows = get(),
            listItemUiModelMapper = get()
        )
    }
}
