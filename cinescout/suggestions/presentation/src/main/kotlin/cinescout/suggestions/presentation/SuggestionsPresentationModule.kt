package cinescout.suggestions.presentation

import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SuggestionsPresentationModule = module {

    viewModel {
        ForYouViewModel(
            forYouMovieUiModelMapper = get(),
            getMovieCredits = get(),
            getSuggestedMovies = get(),
            networkErrorMapper = get()
        )
    }
    factory { ForYouMovieUiModelMapper() }
}
