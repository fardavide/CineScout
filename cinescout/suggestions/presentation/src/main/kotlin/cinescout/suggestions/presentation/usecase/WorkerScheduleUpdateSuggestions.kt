package cinescout.suggestions.presentation.usecase

import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.ScheduleUpdateSuggestions
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker
import org.koin.core.annotation.Factory

@Factory
internal class WorkerScheduleUpdateSuggestions(
    private val scheduler: UpdateSuggestionsWorker.Scheduler
) : ScheduleUpdateSuggestions {

    override fun invoke(suggestionsMode: SuggestionsMode) {
        scheduler(suggestionsMode)
    }
}
