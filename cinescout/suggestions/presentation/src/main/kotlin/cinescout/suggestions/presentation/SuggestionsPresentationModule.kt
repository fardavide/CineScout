package cinescout.suggestions.presentation

import androidx.work.WorkManager
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class SuggestionsPresentationModule {

    @Single
    @Named(ForYouViewModel.SuggestionsStackSizeName)
    @Suppress("FunctionOnlyReturningConstant")
    fun suggestionsStackSizeName() = 10

    @Factory
    fun updateSuggestionsWorkerScheduler(workManager: WorkManager) =
        UpdateSuggestionsWorker.Scheduler(workManager)
}


class SuggestionsPresentationAnnotationModule
