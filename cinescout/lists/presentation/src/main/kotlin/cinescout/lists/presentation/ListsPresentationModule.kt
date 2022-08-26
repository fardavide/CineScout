package cinescout.lists.presentation

import cinescout.lists.presentation.mapper.WatchlistItemUiModelMapper
import cinescout.lists.presentation.viewmodel.WatchlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ListsPresentationModule = module {

    factory { WatchlistItemUiModelMapper() }
    viewModel {
        WatchlistViewModel(
            errorToMessageMapper = get(),
            getAllWatchlistMovies = get(),
            watchlistItemUiModelMapper = get()
        )
    }
}
