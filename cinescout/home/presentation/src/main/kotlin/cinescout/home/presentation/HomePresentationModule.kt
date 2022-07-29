package cinescout.home.presentation

import cinescout.home.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val HomePresentationModule = module {
    viewModel {
        HomeViewModel(
            linkToTmdb = get(),
            linkToTrakt = get(),
            networkErrorMapper = get(),
            notifyTmdbAppAuthorized = get()
        )
    }
}
