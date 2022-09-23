package cinescout.search.presentation

import cinescout.search.presentation.viewmodel.SearchLikedMovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SearchPresentationModule = module {

    viewModel {
        SearchLikedMovieViewModel(
            addMovieToLikedList = get(),
            networkErrorToMessageMapper = get(),
            searchMovies = get()
        )
    }
}
