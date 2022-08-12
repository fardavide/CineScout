package cinescout.suggestions.presentation

import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SuggestionsPresentationModule = module {

    viewModel {
        ForYouViewModel(
            addMovieToDislikedList = get(),
            addMovieToLikedList = get(),
            addMovieToWatchlist = get(),
            forYouMovieUiModelMapper = get(),
            getMovieExtras = get(),
            generateSuggestedMovies = get(),
            networkErrorMapper = get()
        )
    }
    factory { ForYouMovieUiModelMapper() }
}
