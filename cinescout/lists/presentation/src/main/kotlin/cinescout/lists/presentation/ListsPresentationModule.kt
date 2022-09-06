package cinescout.lists.presentation

import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.viewmodel.RatedListViewModel
import cinescout.lists.presentation.viewmodel.WatchlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ListsPresentationModule = module {

    factory { ListItemUiModelMapper() }
    viewModel {
        RatedListViewModel(errorToMessageMapper = get(), getAllRatedMovies = get(), listItemUiModelMapper = get())
    }
    viewModel {
        WatchlistViewModel(
            errorToMessageMapper = get(),
            getAllWatchlistMovies = get(),
            listItemUiModelMapper = get()
        )
    }
}
