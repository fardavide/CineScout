package cinescout.lists.presentation

import cinescout.lists.presentation.viewmodel.WatchlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ListsPresentationModule = module {

    viewModel { WatchlistViewModel(errorToMessageMapper = get(), getAllWatchlistMovies = get()) }
}
