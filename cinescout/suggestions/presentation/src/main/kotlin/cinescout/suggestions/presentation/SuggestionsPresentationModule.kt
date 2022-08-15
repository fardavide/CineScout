package cinescout.suggestions.presentation

import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.usecase.WorkerStartUpdateSuggestedMovies
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val SuggestionsPresentationModule = module {

    viewModel {
        ForYouViewModel(
            addMovieToDislikedList = get(),
            addMovieToLikedList = get(),
            addMovieToWatchlist = get(),
            forYouMovieUiModelMapper = get(),
            getMovieExtras = get(),
            getSuggestedMovies = get(),
            networkErrorMapper = get()
        )
    }
    factory { ForYouMovieUiModelMapper() }
    worker {
        UpdateSuggestionsWorker(
            appContext = get(),
            params = get(),
            analytics = get(),
            ioDispatcher = get(DispatcherQualifier.Io),
            updateSuggestedMovies = get()
        )
    }
    factory { UpdateSuggestionsWorker.Scheduler(workManager = get()) }
    factory<StartUpdateSuggestedMovies> { WorkerStartUpdateSuggestedMovies(scheduler = get()) }
}
