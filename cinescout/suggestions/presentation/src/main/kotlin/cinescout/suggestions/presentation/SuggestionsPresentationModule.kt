package cinescout.suggestions.presentation

import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module

val SuggestionsPresentationModule = module {

    includes(SuggestionsPresentationAnnotationModule().module)

    single(named(ForYouViewModel.SuggestionsStackSizeName)) { 10 }
    worker {
        UpdateSuggestionsWorker(
            appContext = get(),
            params = get(),
            analytics = get(),
            buildUpdateSuggestionsErrorNotification = get(),
            buildUpdateSuggestionsForegroundNotification = get(),
            buildUpdateSuggestionsSuccessNotification = get(),
            ioDispatcher = get(named(DispatcherQualifier.Io)),
            notificationManagerCompat = get(),
            updateSuggestions = get()
        )
    }
    factory { UpdateSuggestionsWorker.Scheduler(workManager = get()) }
}

@Module
@ComponentScan
class SuggestionsPresentationAnnotationModule
