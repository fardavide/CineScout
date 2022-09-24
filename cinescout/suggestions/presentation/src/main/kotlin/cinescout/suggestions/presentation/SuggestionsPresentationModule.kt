package cinescout.suggestions.presentation

import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import cinescout.suggestions.presentation.mapper.ForYouMovieUiModelMapper
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsErrorNotification
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsForegroundNotification
import cinescout.suggestions.presentation.usecase.BuildUpdateSuggestionsSuccessNotification
import cinescout.suggestions.presentation.usecase.CreateUpdateSuggestionsGroup
import cinescout.suggestions.presentation.usecase.WorkerStartUpdateSuggestedMovies
import cinescout.suggestions.presentation.viewmodel.ForYouHintViewModel
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val SuggestionsPresentationModule = module {

    factory {
        BuildUpdateSuggestionsErrorNotification(
            context = get(),
            notificationManagerCompat = get(),
            createUpdateSuggestionsGroup = get()
        )
    }
    factory {
        BuildUpdateSuggestionsForegroundNotification(
            context = get(),
            notificationManagerCompat = get(),
            createUpdateSuggestionsGroup = get()
        )
    }
    factory {
        BuildUpdateSuggestionsSuccessNotification(
            context = get(),
            notificationManagerCompat = get(),
            createUpdateSuggestionsGroup = get()
        )
    }
    factory { CreateUpdateSuggestionsGroup(context = get(), notificationManagerCompat = get()) }
    viewModel { ForYouHintViewModel(setForYouHintShown = get()) }
    viewModel {
        ForYouViewModel(
            addMovieToDislikedList = get(),
            addMovieToLikedList = get(),
            addMovieToWatchlist = get(),
            forYouMovieUiModelMapper = get(),
            getSuggestedMoviesWithExtras = get(),
            networkErrorMapper = get(),
            shouldShowForYouHint = get()
        )
    }
    factory { ForYouMovieUiModelMapper() }
    worker {
        UpdateSuggestionsWorker(
            appContext = get(),
            params = get(),
            analytics = get(),
            buildUpdateSuggestionsErrorNotification = get(),
            buildUpdateSuggestionsForegroundNotification = get(),
            buildUpdateSuggestionsSuccessNotification = get(),
            ioDispatcher = get(DispatcherQualifier.Io),
            notificationManagerCompat = get(),
            updateSuggestedMovies = get()
        )
    }
    factory { UpdateSuggestionsWorker.Scheduler(workManager = get()) }
    factory<StartUpdateSuggestedMovies> { WorkerStartUpdateSuggestedMovies(scheduler = get()) }
}
