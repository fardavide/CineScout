package cinescout.search.presentation

import cinescout.search.presentation.reducer.SearchLikedItemReducer
import cinescout.search.presentation.viewmodel.SearchLikedItemViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SearchPresentationModule = module {

    factory { SearchLikedItemReducer() }
    viewModel {
        SearchLikedItemViewModel(
            addMovieToLikedList = get(),
            addTvShowToLikedList = get(),
            networkErrorToMessageMapper = get(),
            reducer = get(),
            searchMovies = get(),
            searchTvShows = get()
        )
    }
}
