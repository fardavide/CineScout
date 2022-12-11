package cinescout.suggestions.presentation.usecase

import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestions
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker
import org.koin.core.annotation.Factory

@Factory
class WorkerStartUpdateSuggestions(
    private val scheduler: UpdateSuggestionsWorker.Scheduler
) : StartUpdateSuggestions {

    override fun invoke(suggestionsMode: SuggestionsMode) {
        scheduler(suggestionsMode)
    }
}
