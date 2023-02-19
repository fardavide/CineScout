package cinescout.suggestions.domain.usecase

import cinescout.suggestions.domain.model.SuggestionsMode

fun interface StartUpdateSuggestions {

    operator fun invoke(suggestionsMode: SuggestionsMode)
}

class FakeStartUpdateSuggestions : StartUpdateSuggestions {

    var invoked: Boolean = false
        private set

    override operator fun invoke(suggestionsMode: SuggestionsMode) {
        invoked = true
    }
}
