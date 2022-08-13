package cinescout.suggestions.presentation.usecase

import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestedMovies
import cinescout.suggestions.presentation.worker.UpdateSuggestionsWorker

class WorkerStartUpdateSuggestedMovies(
    private val scheduler: UpdateSuggestionsWorker.Scheduler
): StartUpdateSuggestedMovies {

    override fun invoke(suggestionsMode: SuggestionsMode) {
        scheduler(suggestionsMode)
    }
}
