package cinescout.suggestions

import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SuggestionsPresentationModule = module {

    viewModel { ForYouViewModel(getSuggestedMovies = get(), networkErrorMapper = get()) }
}
