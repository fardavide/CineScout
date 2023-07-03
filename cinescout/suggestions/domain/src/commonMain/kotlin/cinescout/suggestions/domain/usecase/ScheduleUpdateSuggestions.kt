package cinescout.suggestions.domain.usecase

import cinescout.suggestions.domain.model.SuggestionsMode

fun interface ScheduleUpdateSuggestions {

    operator fun invoke(suggestionsMode: SuggestionsMode)
}

class FakeScheduleUpdateSuggestions : ScheduleUpdateSuggestions {

    var invoked: Boolean = false
        private set

    override operator fun invoke(suggestionsMode: SuggestionsMode) {
        invoked = true
    }
}
